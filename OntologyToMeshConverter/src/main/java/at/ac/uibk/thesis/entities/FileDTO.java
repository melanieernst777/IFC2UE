package at.ac.uibk.thesis.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

/*
 * We are getting the file as a string, which is not the prettiest solution
 * I tried to do it with multipart files, but c++ and unreal engine do not support this atm
 */
public class FileDTO {

    private String fileName;

    private String fileContent;

}
