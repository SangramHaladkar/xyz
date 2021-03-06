package com.app.monitormymortgage.Activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ResultListener;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.app.monitormymortgage.Adapters.PreviewAdapter;
import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.DataHolderClasses.AttachmentFileData;
import com.app.monitormymortgage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PhotoPreviewActivity extends BaseActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 456;
    private Uri fileUri;
    final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2989;
    private static final int REQUEST_CODE = 6384;
    private Bundle bundle;
    private ArrayList<AttachmentFileData> attachmentFilesList;
    private PreviewAdapter fileAdapter;
    private int position;
    private ImageView imagePreview;
    private ImageView cameraBtn;
    private RecyclerView img_recycler_view;
    boolean is_document_deleted;
    TextView closeButton;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            position = bundle.getInt("position");
            attachmentFilesList = (ArrayList<AttachmentFileData>) bundle.getSerializable(AddMortgageActivity.ATTACHMENTLISTKEY);
        }
        imagePreview = (ImageView) findViewById(R.id.imagePreView);
        cameraBtn = (ImageView) findViewById(R.id.btnCamera);
        img_recycler_view = (RecyclerView) findViewById(R.id.img_recycler_view);
        assert img_recycler_view != null;
        is_document_deleted = false;
        btnCameraClick();
        if (EditMortgageActivity.flag) {
            if (attachmentFilesList.get(position).getDocId() != null) {
                preview(attachmentFilesList.get(position).getDocId());
                fileAdapter = new PreviewAdapter(this, attachmentFilesList);
                setupRecyclerView(img_recycler_view, attachmentFilesList);
            } else {
                setPreview();
            }
        } else {
            setPreview();
        }
        closeButton = (TextView) findViewById(R.id.closeButton);
        closeButtonClick();
    }

    public void btnCameraClick() {
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonConstants.mDebug) Log.v("TAG", "Select Photo");
                int hasWriteExternalStorage = ActivityCompat.checkSelfPermission(PhotoPreviewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && hasWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                } else if (hasWriteExternalStorage == PackageManager.PERMISSION_GRANTED) {
                    openImageFileChooser("Upload Image", "image");
                }
            }
        });
    }

    public void preview(String docId) {

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("doc_id", docId);
        stringObjectHashMap.put("req_from", "android");
        stringObjectHashMap.put("lang", "en");
        ErisCollectionManager.getInstance().callMethod("getAttachmentPreview", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    String str = jsonObjectResult.getString("large_data");
                    if (str.contains("data:image/")) {
                        String[] arrayString = jsonObjectResult.getString("large_data").split("\\,");
                        if (arrayString.length > 0) {
                            str = arrayString[1];
                        }

                        imagePreview.setImageBitmap(decodeBase64String(str));
//                        ViewDialog.hideProgress();
                    } else if (str.contains("pdf")) {
                        Log.v("TAG", "PDF Document");
                        String[] arrayString = jsonObjectResult.getString("large_data").split("\\,");
                        if (arrayString.length > 0) {
                            str = arrayString[1];
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String fileName = "PDF_" + sdf.format(new Date()) + ".pdf";
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
                        file.createNewFile();
                        try {
                            byte[] data1 = Base64.decode(str, 0);
//write the bytes in file
                            if (file.exists()) {
                                OutputStream fo = new FileOutputStream(file);
                                fo.write(data1);
                                fo.flush();
                                fo.close();
                                System.out.println("file created: " + file);
//                                    progressDialog.dismiss();
                                ViewDialog.hideProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
//                            progressDialog.dismiss();
                            ViewDialog.hideProgress();
                        }
                        if (file.length() != 0) {
                            Uri path = Uri.fromFile(file);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(path, "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            Intent intent2 = Intent.createChooser(intent, "Open File");
                            try {
                                startActivity(intent2);
                            } catch (ActivityNotFoundException e) {
                                // Instruct the user to install a PDF reader here, or something
                            }
                            ViewDialog.hideProgress();
//                            progressDialog.dismiss();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, String reason, String details) {

            }
        });
    }

    private void setPreview() {
        fileAdapter = new PreviewAdapter(this, attachmentFilesList);
        imagePreview.setImageBitmap(decodeBase64String(attachmentFilesList.get(position).getFileBase64String()));
        setupRecyclerView(img_recycler_view, attachmentFilesList);
    }

    private Bitmap decodeBase64String(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    public void openImageFileChooser(String msg, String fileType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
        File myDirectory = new File(Environment.getExternalStorageDirectory() + "/M3/");
        if (!myDirectory.exists())
            myDirectory.mkdirs();
        File file = new File(myDirectory, fileName);
        fileUri = Uri.fromFile(file);

        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(fileType + "/*");

        Intent chooserIntent = Intent.createChooser(pickIntent, msg);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{imageIntent});

        try {
            startActivityForResult(chooserIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } catch (Exception e) {
            // The reason for the existence of aFileChooser
            e.printStackTrace();
        }

    }

    public void openFileExplorer() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("pdf/*");
        Intent intent = Intent.createChooser(getIntent, getString(R.string.app_name));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (Exception e) {
            Log.i("MainActivity.java", "" + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                try {
                    Uri imageUri = fileUri;
                    Bitmap scaledOriginalImage;
                    if (imageUri != null && (data == null || data.getData() == null)) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;

                        AssetFileDescriptor fileDescriptor;

                        fileDescriptor = this.getContentResolver().openAssetFileDescriptor(imageUri, "r");

                        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                        int bitmapWidth = bitmap.getWidth();
                        if (CommonConstants.mDebug) Log.v("TAG", "bitmap width" + bitmapWidth);
                        if (bitmapWidth > 1024) {
                            //create scaled image from original image.Resize width to 1024.
                            scaledOriginalImage = Bitmap.createScaledBitmap(bitmap, 1024, bitmap.getHeight(), false);
                        } else {
                            scaledOriginalImage = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                        }
                        // create thumbnail of image/bitmap captured from camera.
                        Bitmap thumb = Bitmap.createScaledBitmap(scaledOriginalImage, 100, 100, false);
                        if (CommonConstants.mDebug) Log.v("TAG", "thumbWidth" + thumb.getWidth());
                        if (CommonConstants.mDebug) Log.v("TAG", "thumbHeight" + thumb.getHeight());
                        Bitmap rotatedImage = roatedBitmap(fileUri.getPath(), scaledOriginalImage);
                        final String path = FileUtils.getPath(this, imageUri);
                        String fileName = getFileNameByUri(this, imageUri);
                        File file = new File(path);
                        double fileLength = file.length();
                        double fileSizeKb = (fileLength / 1024);
                        double fileSizeMb = (fileSizeKb / 1024);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        scaledOriginalImage.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        byte[] bitmapData = stream.toByteArray();


                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        thumb.compress(Bitmap.CompressFormat.JPEG, 50, stream1);
                        byte[] thumbData = stream1.toByteArray();

                        if (fileSizeMb <= 5) {
                            AttachmentFileData attachmentFileData = new AttachmentFileData();
                            attachmentFileData.setFileName(fileName);
                            attachmentFileData.setFileSize("" + Math.round(fileSizeKb));
                            attachmentFileData.setFileBase64String(Base64.encodeToString(bitmapData, Base64.DEFAULT));
                            attachmentFileData.setFileType(getMimeType(fileName));
                            attachmentFileData.setThumbNailBase64String(Base64.encodeToString(thumbData, Base64.DEFAULT));
//                                attachmentFileData.setImageBitMap(bitmap);
                            attachmentFilesList.add(attachmentFileData);
                            setupRecyclerView(img_recycler_view, attachmentFilesList);

                        } else {
                            ViewDialog.showAlertPopUp(PhotoPreviewActivity.this, getResources().getString(R.string.file_upload_limit), getResources().getString(R.string.error));
//                            Toast.makeText(this, "Unable to attach", Toast.LENGTH_LONG).show();
                        }

                    } else if (data != null && data.getData() != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        if (CommonConstants.mDebug)
                            Log.i("MainActivityResult", "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            Bitmap rotatedImage;
                            Bitmap scaledOriginalImageLarge;
                            File file = new File(path);
                            String fileName = file.getName().replaceAll("\\s", "%20");
                            double fileLength = file.length();
                            double fileSizeKb = (fileLength / 1024);
                            double fileSizeMb = (fileSizeKb / 1024);
                            Bitmap bitmap = (BitmapFactory.decodeFile(path));
                            if (CommonConstants.mDebug)
                                Log.v("TAG", "bitmap width" + bitmap.getWidth());
                            if (bitmap.getWidth() > 1024) {
                                scaledOriginalImageLarge = Bitmap.createScaledBitmap(bitmap, 1024, bitmap.getHeight() / 3, false);
                                rotatedImage = roatedBitmap(path, scaledOriginalImageLarge);
                                Log.v("TAG", "rotatedImageWidth" + rotatedImage.getWidth());
                                Log.v("TAG", "rotatedImageHeight" + rotatedImage.getHeight());
                            } else {
                                scaledOriginalImageLarge = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                                rotatedImage = roatedBitmap(path, scaledOriginalImageLarge);
                                Log.v("TAG", "rotatedImageWidth" + rotatedImage.getWidth());
                                Log.v("TAG", "rotatedImageHeight" + rotatedImage.getHeight());

                            }
                            Bitmap thumb = Bitmap.createScaledBitmap(scaledOriginalImageLarge, 100, 100, false);

                            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                            thumb.compress(Bitmap.CompressFormat.JPEG, 50, stream1);
                            byte[] thumbData = stream1.toByteArray();

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            if (getMimeType(fileName).contains("png") || getMimeType(fileName).contains("jpg") || getMimeType(fileName).contains("jpeg")) {
                                if (getMimeType(fileName).contains("png")) {
                                    rotatedImage.compress(Bitmap.CompressFormat.PNG, 50, stream);
                                } else {
                                    rotatedImage.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                }
                                byte[] bitmapData2 = stream.toByteArray();

                                if (fileSizeMb <= 5) {
                                    AttachmentFileData attachmentFileData = new AttachmentFileData();
                                    attachmentFileData.setFileName(fileName);
                                    attachmentFileData.setFileSize("" + Math.round(fileSizeKb));
                                    attachmentFileData.setFileBase64String(Base64.encodeToString(bitmapData2, Base64.DEFAULT));
                                    attachmentFileData.setThumbNailBase64String(Base64.encodeToString(thumbData, Base64.DEFAULT));
                                    attachmentFileData.setFileType(getMimeType(fileName));
//                                        attachmentFileData.setImageBitMap(bitmap);
                                    attachmentFilesList.add(attachmentFileData);
                                    setupRecyclerView(img_recycler_view, attachmentFilesList);
                                } else {
                                    ViewDialog.showAlertPopUp(PhotoPreviewActivity.this, getResources().getString(R.string.file_upload_limit), getResources().getString(R.string.error));
                                }
                            } else {
                                ViewDialog.showAlertPopUp(PhotoPreviewActivity.this, getResources().getString(R.string.supported_file_formats), getResources().getString(R.string.error));
                            }
                        } catch (Exception e) {
                            if (CommonConstants.mDebug)
                                Log.e("MainActivityResult", "File select error" + e.getMessage());
                        }
                    } else {
//                            attachImgFile.setImageDrawable(this.getResources().getDrawable(R.drawable.avatar));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public static String getFileNameByUri(Context context, Uri uri) {
        String fileName = "unknown";//default fileName
        Uri filePathUri = uri;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                filePathUri = Uri.parse(cursor.getString(column_index));
                fileName = filePathUri.getLastPathSegment().toString();
            }
        } else if (uri.getScheme().compareTo("file") == 0) {
            fileName = filePathUri.getLastPathSegment().toString();
        } else {
            fileName = fileName + "_" + filePathUri.getLastPathSegment();
        }
        return fileName;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension.toLowerCase());
            if (type == null) {
                type = extension;
            }
        }
        return type;
    }

    public Bitmap roatedBitmap(String filePath, Bitmap bitmap) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();

        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView, ArrayList<AttachmentFileData> fileDataArrayList) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(fileAdapter);
        fileAdapter.setCallBack(new PreviewAdapter.RecyclerViewListeners() {
            @Override
            public void onRecycleItemClick(ArrayList<AttachmentFileData> filesDataList, int position) {
                if (EditMortgageActivity.flag) {
                    if (filesDataList.get(position).getDocId() != null) {
                        preview(filesDataList.get(position).getDocId());
                    } else {
                        imagePreview.setImageBitmap(decodeBase64String(filesDataList.get(position).getFileBase64String()));
                    }
                } else {
                    imagePreview.setImageBitmap(decodeBase64String(filesDataList.get(position).getFileBase64String()));
                }
            }

            @Override
            public void onCloseIconClick(ArrayList<AttachmentFileData> filesDataList, int position) {
                filesDataList.remove(position);
                is_document_deleted = true;
                fileAdapter.notifyDataSetChanged();
                if (filesDataList.size() >= 5) {
                    cameraBtn.setVisibility(View.GONE);
                } else {
                    cameraBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        if (fileDataArrayList.size() >= 5) {
            cameraBtn.setVisibility(View.GONE);
        } else {
            cameraBtn.setVisibility(View.VISIBLE);
        }
    }

    public void closeButtonClick() {
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isDocumentDeleted", is_document_deleted);
                bundle.putSerializable(AddMortgageActivity.ATTACHMENTLISTKEY, attachmentFilesList);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isDocumentDeleted", is_document_deleted);
        bundle.putSerializable(AddMortgageActivity.ATTACHMENTLISTKEY, attachmentFilesList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
