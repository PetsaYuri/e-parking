package API.eparking.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class UploadsImage {

    private final String PATH_TO_UPLOADS = System.getProperty("user.dir") + "/uploads/";

    public Boolean saveImage(MultipartFile file, String filename) {
        try {
            file.transferTo(new File(PATH_TO_UPLOADS + filename));
            return true;
        } catch (Exception ex)  {
            ex.printStackTrace();
        }
        return false;
    }

    public String createUUIDFilename(String originalFilename)  {
        try {
            return UUID.randomUUID().toString() + "." + originalFilename.split("\\.", 2)[1];
        } catch (IndexOutOfBoundsException ex)  {
            return UUID.randomUUID().toString() + ".jpg";
        }
    }
}
