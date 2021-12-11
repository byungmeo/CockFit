package com.kbd.cockfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private Uri deviceImageUri;

    private static final int PICK_FROM_ALBUM =1;

    private String uid;
    private TextView textView_nickname;
    private TextView textView_userEmail;
    private TextView textView_userRegisterDate;
    private ImageView imageview_profileImage;
    private ImageButton imageButton_changeName;
    private Button button_logout;
    private Button button_userInfo;

    private Button button_myFavorite;
    private Button button_myCommunityActivity;
    private Button button_bookmarkRecipePost;

    private EditText editText_currentPw;
    private Button button_cancel;
    private Button button_ok;
    private AlertDialog userCheckDialog;

    private ProgressBar progressBar;

    private Button button_notification;
    private Toolbar toolbar;

    private Long mLastClickTime = 0L;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        progressBar = v.findViewById(R.id.profile_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        user = mAuth.getCurrentUser();
        uid = user.getUid();

        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cock-fit-ebaa7.appspot.com");

        textView_nickname = v.findViewById(R.id.profile_textView_name);
        textView_userEmail = v.findViewById(R.id.profile_textView_userEmail);
        textView_userRegisterDate = v.findViewById(R.id.profile_textView_userRegisterDate);
        imageview_profileImage = v.findViewById(R.id.profile_imageView_photo);
        imageButton_changeName = v.findViewById(R.id.profile_imageButton_changeName);
        button_userInfo = v.findViewById(R.id.profile_imageButton_userInfo);
        button_logout = v.findViewById(R.id.profile_imageButton_logout);
        button_myFavorite = v.findViewById(R.id.profile_button_bookmarkBasicRecipe);
        button_myCommunityActivity = v.findViewById(R.id.profile_button_communityActivity);
        button_bookmarkRecipePost = v.findViewById(R.id.profile_button_bookmarkSharePost);
        textView_userEmail.setText(user.getEmail());




        toolbar = v.findViewById(R.id.topAppBarFragment);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_notify) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, NotificationActivity.class);
                    context.startActivity(intent);
                }
                return false;
            }
        });



        mDatabase.child("user").child(uid).child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String userRegisterDate = dataSnapshot.getValue(String.class);
                    textView_userRegisterDate.setText("가입 일자 : " + userRegisterDate);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Glide.with(this)
                .load(user.getPhotoUrl())
                .thumbnail(0.1f)
                .into(imageview_profileImage);

        textView_nickname.setText(user.getDisplayName()); //닉네임 표시

        imageButton_changeName.setOnClickListener(this);
        button_userInfo.setOnClickListener(this);
        button_logout.setOnClickListener(this);
        button_myFavorite.setOnClickListener(this);
        button_myCommunityActivity.setOnClickListener(this);
        button_bookmarkRecipePost.setOnClickListener(this);
        imageview_profileImage.setOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mDatabase.child("user").child(uid).child("notify").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0) {
                    toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_baseline_notification_important_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateProfilePhoto(Uri devicePhotoUri){
        progressBar.setVisibility(View.VISIBLE);
        StorageReference photoReference = mStorage.child("Users/"+uid+"/profileImage.jpg");
        photoReference.putFile(devicePhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                photoReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Log.d("test", "프로필 사진 업데이트 완료");
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                        //Picasso.get().load(uri).into(imageview_profileImage);
                        Glide.with(ProfileFragment.this)
                                .load(uri)
                                .into(imageview_profileImage);
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
                deviceImageUri = data.getData();
                updateProfilePhoto(deviceImageUri);
            }
        }
    }

    @Override
    public void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        if(elapsedTime > 600) {
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
                                                textView_nickname.setText(userChangeNic);
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
                case R.id.profile_button_communityActivity: {
                    Intent intent = new Intent(context, ForumActivity.class);
                    intent.putExtra("forumType", "myPost");
                    context.startActivity(intent);
                    break;
                }
                case R.id.action_notify: {
                    Intent intent = new Intent(context, NotificationActivity.class);
                    context.startActivity(intent);
                    break;
                }
                case R.id.profile_button_bookmarkBasicRecipe: {
                    Intent intent = new Intent(context, ListActivity.class);
                    intent.putExtra("keyword", "favorite");
                    context.startActivity(intent);
                    break;
                }
                case R.id.profile_button_bookmarkSharePost: {
                    Intent intent = new Intent(context, ForumActivity.class);
                    intent.putExtra("forumType", "bookmarkSharePost");
                    context.startActivity(intent);
                    break;
                }
                case R.id.profile_imageButton_logout: {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getContext().startActivity(intent);
                    break;
                }
                case R.id.profile_imageButton_userInfo: {
                    //사용자 정보 변경 화면으로 넘어가기
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.usercheck, null);

                    editText_currentPw = dialogView.findViewById(R.id.userCheck_editText_currentPw);
                    button_cancel = dialogView.findViewById(R.id.userCheck_button_cancel);
                    button_ok = dialogView.findViewById(R.id.userCheck_button_ok);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);
                    userCheckDialog = builder.create();
                    userCheckDialog.show();

                    button_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String currentPw = editText_currentPw.getText().toString();
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPw);
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(context, UserAdminActivity.class);
                                        context.startActivity(intent);
                                        userCheckDialog.dismiss();
                                    } else {
                                        Toast.makeText(context, "비밀번호가 잘못되었습니다", Toast.LENGTH_SHORT).show();
                                        userCheckDialog.dismiss();
                                    }
                                }
                            });
                        }
                    });
                    button_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userCheckDialog.dismiss();
                        }
                    });
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
}