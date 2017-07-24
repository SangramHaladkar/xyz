package com.app.monitormymortgage.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ResultListener;
import com.app.monitormymortgage.Adapters.AttachmentPreviewAdapter;
import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.DataHolderClasses.AttachmentFileData;
import com.app.monitormymortgage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ShowImagesActivity extends BaseActivity {

    String mortgageId;
    private ArrayList<AttachmentFileData> attachmentFilesList;
    private AttachmentPreviewAdapter attachmentPreviewAdapter;
    private RecyclerView img_recycler_view;
    private ImageView imagePreview;
    private ImageView cameraBtn;
    String doc_id;
    ProgressDialog progressDialog;
    TextView closeButton;
    public static boolean flagChanged=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        progressDialog = new ProgressDialog(this);
        ViewDialog.showProgress(R.string.help_screen_one, ShowImagesActivity.this, getResources().getString(R.string.please_wait));
//        showProgress("");
        mortgageId = getIntent().getExtras().getString("mortgageID");
        doc_id = getIntent().getExtras().getString("doc_id");
        attachmentFilesList = new ArrayList<>();
        Log.v("TAG", mortgageId);
        getThumbnailData(mortgageId);
        showPreview(doc_id);
        img_recycler_view = (RecyclerView) findViewById(R.id.img_recycler_view);
        assert img_recycler_view != null;
        imagePreview = (ImageView) findViewById(R.id.imagePreView);
        cameraBtn = (ImageView) findViewById(R.id.btnCamera);
        cameraBtn.setVisibility(View.GONE);
        closeButton=(TextView)findViewById(R.id.closeButton);
//        ViewDialog.showProgress(R.string.help_screen_one, ShowImagesActivity.this, R.string.progress_wait_while_loading);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagChanged=true;
                finish();
            }
        });
    }

    public void getThumbnailData(String mortgageId) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("mortgage_id", mortgageId);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("getMortgageDetails", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v("TAG onsuccess", result);

                try {
                    JSONObject jsonObjectResult = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectResult.getJSONObject("data");
                    JSONObject jsonObjectResultMain = jsonObjectData.getJSONObject("result");
                    JSONArray jsonArrayThumbnailData = jsonObjectResultMain.getJSONArray("attachments");
                    for (int i = 0; i < jsonArrayThumbnailData.length(); i++) {
                        AttachmentFileData attachmentFileData = new AttachmentFileData();
                        JSONObject jsonObjectRow = jsonArrayThumbnailData.getJSONObject(i);
                        attachmentFileData.setDocId(jsonObjectRow.getString("doc_id"));
                        doc_id = jsonObjectRow.getString("doc_id");
                        attachmentFileData.setThumbNailBase64String(jsonObjectRow.getString("thumbnail_data"));
                        attachmentFilesList.add(attachmentFileData);
                    }

                    setPreview();
//                    ViewDialog.hideProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.v("TAG onerror", reason);

            }
        });
    }

    private void setPreview() {
        attachmentPreviewAdapter = new AttachmentPreviewAdapter(this, attachmentFilesList);
//        imagePreview.setImageBitmap(decodeBase64String(attachmentFilesList.get(position).getFileBase64String()));
        setupRecyclerView(img_recycler_view);

    }

    private Bitmap decodeBase64String(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(attachmentPreviewAdapter);
        attachmentPreviewAdapter.setCallBack(new AttachmentPreviewAdapter.RecyclerViewListeners() {
            @Override
            public void onRecycleItemClick(ArrayList<AttachmentFileData> filesDataList, int position) {
//                showProgress("");
                ViewDialog.showProgress(R.string.help_screen_one, ShowImagesActivity.this, getResources().getString(R.string.please_wait));
                showPreview(filesDataList.get(position).getDocId());
            }

            @Override
            public void onCloseIconClick(ArrayList<AttachmentFileData> filesDataList, int position) {
                filesDataList.remove(position);
                attachmentPreviewAdapter.notifyDataSetChanged();
//                if (filesDataList.size() >= 5) {
//                    cameraBtn.setVisibility(View.GONE);
//                } else {
//                    cameraBtn.setVisibility(View.VISIBLE);
//                }
            }
        });
//        if (fileDataArrayList.size() >= 5) {
//            cameraBtn.setVisibility(View.GONE);
//        } else {
//            cameraBtn.setVisibility(View.VISIBLE);
//        }
    }

    public void showPreview(String doc_id) {
//        ViewDialog.showProgress(R.string.help_screen_one,ShowImagesActivity.this,R.string.progress_wait_while_loading);
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("doc_id", doc_id);
        stringObjectHashMap.put("req_from", "android");
        stringObjectHashMap.put("lang", "en");
        ErisCollectionManager.getInstance().callMethod("getAttachmentPreview", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v("TAG", result);
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    String str = jsonObjectResult.getString("large_data");
                    if (str.contains("data:image/")) {
                        Log.v("TAG", "Image Document");
                        String[] arrayString = jsonObjectResult.getString("large_data").split("\\,");
                        if (arrayString.length > 0) {
                            str = arrayString[1];
                        }

                        imagePreview.setImageBitmap(decodeBase64String(str));
                        ViewDialog.hideProgress();
//                        progressDialog.dismiss();
                    } else if (str.contains("pdf")) {
                        Log.v("TAG", "PDF Document");
                        String[] arrayString = jsonObjectResult.getString("large_data").split("\\,");
                        if (arrayString.length > 0) {
                            str = arrayString[1];
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String fileName = "PDF_" + sdf.format(new Date()) + ".pdf";
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),fileName);
                        file.createNewFile();
                        try {
                            byte[] data1 = Base64.decode(str,0);
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
                } catch (FileNotFoundException e) {
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

    public void showProgress(String message) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {
        flagChanged=true;
        finish();
    }
}
