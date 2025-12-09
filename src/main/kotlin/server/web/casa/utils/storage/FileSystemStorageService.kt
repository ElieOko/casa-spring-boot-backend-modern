package server.web.casa.utils.storage

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream
import kotlin.io.path.name

@Service
class FileSystemStorageService(private val properties: StorageProperties) : StorageService {

    private var rootLocation: Path

    init {
        if (properties.location.trim().isEmpty()) {
            throw StorageException("File upload location can not be Empty.")
        }
        this.rootLocation = Paths.get(properties.location)
    }

    override fun store(file: MultipartFile, subfolder: String): String {
        try {
            this.rootLocation = Paths.get("${properties.location}/$subfolder")
            if (file.isEmpty) {
                throw StorageException("Failed to store empty file.")
            }

            val filename = file.originalFilename
                ?: throw StorageException("Failed to store file with null filename")

            val destinationFile = rootLocation.resolve(filename)
                .normalize().toAbsolutePath()

            if (!destinationFile.parent.equals(rootLocation.toAbsolutePath())) {
                throw StorageException("Cannot store file outside current directory.")
            }

            file.inputStream.use { inputStream ->
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }

            return filename  // 🔥 Retourner le nom du fichier

        } catch (e: IOException) {
            throw StorageException("Failed to store file ${file.originalFilename}", e)
        }
    }


    override fun loadAll(): Stream<Path> {
        return try {
            Files.walk(rootLocation, 1)
                .filter { path -> path != rootLocation }
                .map { path -> rootLocation.relativize(path) }
        } catch (e: IOException) {
            throw StorageException("Failed to read stored files", e)
        }
    }

    override fun load(filename: String): Path {
        return rootLocation.resolve(filename)
    }

    override fun loadAsResource(filename: String): Resource? {
        return try {
            val file = load(filename)
            val resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageException("Could not read file: $filename")
            }
        } catch (e: MalformedURLException) {
            throw StorageException("Could not read file: $filename", e)
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }
    }
}