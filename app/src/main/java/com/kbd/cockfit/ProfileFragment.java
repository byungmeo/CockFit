package com.kbd.cockfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private StorageReference profileRef;
    private FirebaseUser user;
    private Uri file;

    private static final int PICK_FROM_ALBUM =1;

    private String uid;
    private TextView editText_nickname;
    private ImageView imageview_profileImage;
    private ImageButton imageButton_changeName;
    private ImageButton imageButton_logout;

    private Button button_myFavorite;
    private Button button_myCommunityActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();
        storageRef = FirebaseStorage.getInstance().getReference();
        profileRef = storageRef.child("users/"+uid+"profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageview_profileImage);
            }
        });

        editText_nickname = v.findViewById(R.id.profile_textView_name);
        imageview_profileImage = v.findViewById(R.id.profile_imageView_photo);
        imageButton_changeName = v.findViewById(R.id.profile_imageButton_changeName);
        imageButton_logout = v.findViewById(R.id.profile_imageButton_logout);

        button_myFavorite = v.findViewById(R.id.profile_button_recipe);
        button_myCommunityActivity = v.findViewById(R.id.profile_button_communityActivity);

        editText_nickname.setText(user.getDisplayName());

        imageButton_changeName.setOnClickListener(this);
        imageButton_logout.setOnClickListener(this);
        button_myFavorite.setOnClickListener(this);
        button_myCommunityActivity.setOnClickListener(this);
        imageview_profileImage.setOnClickListener(this);

        return v;
    }

    //이미지 firebase store에 올리기
    private void uploadImageToFirebase(Uri file){
        StorageReference fileRef = storageRef.child("users/"+uid+"profile.jpg");
        fileRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageview_profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    //앨범에서 프로필 이미지uri 받아와 imageview 변경
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(PICK_FROM_ALBUM == 1){
            if(resultCode == Activity.RESULT_OK){
                file = data.getData();
                uploadImageToFirebase(file);
            }
        }
    }


    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        switch (v.getId()) {
            case R.id.profile_imageButton_changeName: {
                final EditText editText_changeNic = new EditText(context);

                FrameLayout container = new FrameLayout(context);
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                editText_changeNic.setLayoutParams(params);
                container.addView(editText_changeNic);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

                alertBuilder.setTitle("닉네임을 변경해주세요");
                alertBuilder.setView(container);

                alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final FirebaseUser user = mAuth.getCurrentUser();
                        String userChangeNic = editText_changeNic.getText().toString();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userChangeNic)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(context, "닉네임이 변경되었습니다", Toast.LENGTH_SHORT).show();
                                            editText_nickname.setText(userChangeNic);
                                        }
                                    }
                                });
                    }
                });

                alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(context, "닉네임이 변경되지 않았습니다", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();

                break;
            }


            case R.id.profile_button_recipe: {
                //즐겨찾기 기능 개발 후 작성예정
//                Intent intent = new Intent(context, ListActivity.class);
//                intent.putExtra("keyword", "favorite");
//                context.startActivity(intent);
                break;
            }
            case R.id.profile_button_communityActivity: {
                Intent intent = new Intent(context, ForumActivity.class);
                intent.putExtra("forum", "myPost");
                context.startActivity(intent);
                break;
            }
            case R.id.profile_imageButton_logout: {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                break;
            }
            case R.id.profile_imageView_photo: {
                //앨범으로 이동해서 Uri가져오기
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
                break;
            }

        }
    }
}