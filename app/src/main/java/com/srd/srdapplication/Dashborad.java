package com.srd.srdapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Dashborad extends AppCompatActivity {
    Button camera;
    Button gallery;
    ImageView im;
    int imageSize = 400;
    public static String imageFileName;
    FirebaseAuth mAuth;
    FirebaseFirestore database;
    public String urlImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashborad);
        camera = findViewById(R.id.camera);
        gallery = findViewById(R.id.gallery);
        im = findViewById(R.id.im);
        imageFileName = String.valueOf(System.currentTimeMillis());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);

                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);

                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                Uri m=cameraIntent.getData();
//                Log.d("m" , String.valueOf(m));
                startActivityForResult(cameraIntent, 1);
//                UploadFileToServer up= new UploadFileToServer();
//                up.doInBackground(SERVER_ADDRESS);

            }
        });

    }


    public void classifyImage(Bitmap image) {
        //   try {
//            Model model = Model.newInstance(getApplicationContext());
//
//            // Creates inputs for reference.
//            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 12, 12, 512}, DataType.FLOAT32);
//            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
//            byteBuffer.order(ByteOrder.nativeOrder());
//
//            int[] intValues = new int[imageSize * imageSize];
//            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
//            int pixel = 0;
//            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
//            for(int i = 0; i < imageSize; i ++){
//                for(int j = 0; j < imageSize; j++){
//                    int val = intValues[pixel++]; // RGB
//                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
//                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
//                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
//                }
//            }
//
//            inputFeature0.loadBuffer(byteBuffer);
//
//            // Runs model inference and gets result.
//            Model.Outputs outputs = model.process(inputFeature0);
//            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//            float[] confidences = outputFeature0.getFloatArray();
//            // find the index of the class with the biggest confidence.
//            int maxPos = 0;
//            float maxConfidence = 0;
//            for (int i = 0; i < confidences.length; i++) {
//                if (confidences[i] > maxConfidence) {
//                    maxConfidence = confidences[i];
//                    maxPos = i;
//                }
//            }
//            String[] classes =  {"AtopicDermatitis", "Lupus", "PityriasisRosea","Psoriasis","Urticaria"};
//            txt.setText(classes[maxPos]);
//
//            // Releases model resources if no longer used.
//            model.close();
//        } catch (IOException e) {
//            // TODO Handle the exception
        //}
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap image;

        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                image = (Bitmap) data.getExtras().get("data");
//                UploadImage(imageFileUri.getPath());
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                im.setImageBitmap(image);
//
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image);
            } else {
                Uri dat = data.getData();
                image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                    im.setImageBitmap(image);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            Bitmap bitmap = ((BitmapDrawable) im.getDrawable()).getBitmap();
            uploadImage(bitmap, "pre.jpeg", new UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if (url == null) {
                        urlImage = url;
                    } else {
                        urlImage = url;
                        //mAuth.getCurrentUser();
                        FirebaseUser mUser = mAuth.getCurrentUser();
                        String uId = mUser.getUid();
                        DocumentReference ref = database.collection("user").document(uId);
                        ref.update("url", url).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("Updated");
                            }
                        });
                    }

                    OkHttpClient okHttpClient;
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.MINUTES)
                            .writeTimeout(10, TimeUnit.MINUTES)
                            .readTimeout(30, TimeUnit.MINUTES)
                            .build();
                    //https://sersrd.herokuapp.com/
                    RequestBody formBody = new FormBody.Builder().add("url", urlImage).add("id", mAuth.getUid()).build();
                    Request request = new Request.Builder().url("http://192.168.43.158:5000/").post(formBody).build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Dashborad.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final String message=response.body().string();
                            final String tx="AtopicDermatitis";
                            //final TextView txt = findViewById(R.id.txt);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    try {
                                        //txt.setText(message);
                                        //response.body().close();
                                        //txt.setText(response.body().string());
                                        //txt.setText(message);
//                                        String str=response.body().string();

                                    switch(message) {
                                        case "AtopicDermatitis":
                                            Intent intent = new Intent(getApplicationContext(), AtopicDermatitis.class);
                                            startActivity(intent);
                                            break;
                                        case "lupus":
                                            intent = new Intent(getApplicationContext(), Lupus.class);
                                            startActivity(intent);
                                            break;
                                        case "PityriasisRosea":
                                            intent = new Intent(getApplicationContext(), PityriasisRosea.class);
                                            startActivity(intent);
                                            break;
                                        case "Psoriasis":
                                            intent = new Intent(getApplicationContext(), Psoriasis.class);
                                            startActivity(intent);
                                            break;
                                        case "Urticaria":
                                            intent = new Intent(getApplicationContext(), Urticaria.class);
                                            startActivity(intent);
                                            break;
                                        default:
                                            intent = new Intent(getApplicationContext(), Dashborad.class);
                                            startActivity(intent);
                                            break;
                                    }




//                                        if(message.equals(tx)) {
//                                            Intent intent = new Intent(getApplicationContext(), AtopicDermatitis.class);
//                                            startActivity(intent);
//                                        }
//                                        else{
//                                            Intent intent = new Intent(getApplicationContext(), Login.class);
//                                            startActivity(intent);
//                                        }
                                    //startActivity(new Intent(getApplicationContext(),Register.class));
//                                        if(str=="ba")
//                                            startActivity(new Intent(getApplicationContext(),Login.class));

//                                        if(txt.getText() =="ba")
//                                            startActivity(new Intent(getApplicationContext(),Login.class));

//
//                                     catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
                                }

                            });
                        }

                    });
                    //txt.setText(urlImage);
                    Log.i("url", url);
                    Log.i("urlImage", urlImage);

                }
            });

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

            // classifyImage(image);
            //}
        }
        //txt.setText(urlImage);

        super.onActivityResult(requestCode, resultCode, data);


    }

    interface UploadImageListener {
        public void onComplete(String url);
    }

    public void uploadImage(Bitmap imageBmp, String fileName, final UploadImageListener listener) {
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("images").child(uId).child(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }



}