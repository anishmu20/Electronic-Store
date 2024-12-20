package pros.ElectronicStore.services.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pros.ElectronicStore.exceptions.BadApiRequestException;
import pros.ElectronicStore.services.FileService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImplementation implements FileService {

    private Logger  logger=LoggerFactory.getLogger(FileServiceImplementation.class);
    @Override
    public String UploadFile(MultipartFile file, String path) throws IOException {

        String originalFileName=file.getOriginalFilename();
        logger.info("FileName {}",originalFileName);
        String filename= UUID.randomUUID().toString();
        String extension=originalFileName.substring(originalFileName.lastIndexOf("."));
        String FileNameWithExtension=filename+extension;
        logger.info(FileNameWithExtension);
        String FullPathWithFileName=Paths.get(path,FileNameWithExtension).toString();
        logger.info(FullPathWithFileName);
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")){
            // file save
            File folder=new File(path);
            if (!folder.exists()){
                // create folder if not exists
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(FullPathWithFileName));
            return FileNameWithExtension;

        }else{
            throw new BadApiRequestException("File with this "+extension+" not allowed");
        }


    }

    @Override
    public InputStream getResource(String path,String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        logger.info(fullPath);
        InputStream inputStream= new FileInputStream(fullPath);
        return inputStream;
    }
}
