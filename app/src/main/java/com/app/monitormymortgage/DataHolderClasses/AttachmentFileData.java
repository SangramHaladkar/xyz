package com.app.monitormymortgage.DataHolderClasses;

import java.io.Serializable;

/**
 * Created by Raj on 24/05/16.
 */
public class AttachmentFileData implements Serializable {

    private String fileName;
    private String fileSize;
    private String fileBase64String;
    //    private Bitmap imageBitMap;
    private String fileType;
    private String fileBase64StringWithType;
    private String thumbNailBase64String;
    private String thumbNailBase64StringWithType;
    private String docId;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public AttachmentFileData() {
    }

    public String getThumbNailBase64String() {
        return thumbNailBase64String;
    }

    public void setThumbNailBase64String(String thumbNailBase64String) {
        this.thumbNailBase64String = thumbNailBase64String;
    }



    public void setThumbNailBase64StringWithType(String thumbNailBase64StringWithType) {
        this.thumbNailBase64StringWithType = thumbNailBase64StringWithType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileBase64String() {
        return fileBase64String;
    }

    public void setFileBase64String(String fileBase64String) {
        this.fileBase64String = fileBase64String;
    }


    public String getFileBase64StringWithType() {
        fileBase64StringWithType = "data:" + fileType + ";base64," + fileBase64String;
        return fileBase64StringWithType;
    }
    public String getThumbNailBase64StringWithType() {
        thumbNailBase64StringWithType="data:"+fileType+";base64,"+thumbNailBase64String;
        return thumbNailBase64StringWithType;
    }


}
