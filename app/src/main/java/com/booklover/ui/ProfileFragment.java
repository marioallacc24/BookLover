package com.booklover.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booklover.R;
import com.booklover.dao.DAOUser;
import com.booklover.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private ImageView mProfileImage;
    private TextView mFullName,mRole,mBooks;
    private TextInputLayout mName,mEmail,mPassword;
    private Button mUpdate;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ProgressDialog mDialog;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProfileImage = view.findViewById(R.id.profile_image);
        mFullName = view.findViewById(R.id.fullname_field);
        mRole = view.findViewById(R.id.role_field);
        mBooks = view.findViewById(R.id.books_label);
        mName = view.findViewById(R.id.name_profile);
        mEmail = view.findViewById(R.id.email_profile);
        mPassword = view.findViewById(R.id.password_profile);
        mUpdate = view.findViewById(R.id.update_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mDialog = new ProgressDialog(getContext());


        mProfileImage.setOnClickListener(v -> choosePicture());

        showUserData();

        mUpdate.setOnClickListener(v -> updateUserData());


    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData()!=null){
            imageUri = data.getData();
            mProfileImage.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        mDialog.setMessage("Upload...");
        mDialog.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    mDialog.dismiss();
                    Toast.makeText(getContext(),"Uspesno!",Toast.LENGTH_SHORT).show();

                    riversRef.getDownloadUrl().addOnSuccessListener(uri -> updateUserPicture(uri.toString()));


                })
                .addOnFailureListener(exception -> {
                    mDialog.dismiss();
                    Toast.makeText(getContext(),"Greska prilikom uploada!",Toast.LENGTH_SHORT).show();
                });
    }

    private void showUserData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String name = userProfile.getName();
                    String rola = userProfile.getRola();
                    String numberOfBooks = String.valueOf(userProfile.getNumberOfBooks());
                    String email = userProfile.getEmail();
                    String profilePicture = userProfile.getProfilePicture();


                    mFullName.setText(name);
                    mRole.setText(rola);
                    mBooks.setText(numberOfBooks);
                    mName.getEditText().setText(name);
                    mEmail.getEditText().setText(email);

                    Glide.with(getView())
                            .load(profilePicture)
                            .into(mProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Greska!!!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData() {
        DAOUser dao = new DAOUser();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("name",mName.getEditText().getText().toString());
        hashMap.put("email",mEmail.getEditText().getText().toString());

        dao.update(hashMap).addOnFailureListener(err -> {
            Toast.makeText(getContext(),""+err.getMessage(),Toast.LENGTH_LONG).show();
        }).addOnSuccessListener(suc -> {
            Toast.makeText(getContext(),"Uspesno!",Toast.LENGTH_LONG).show();
        });
        
        String email = mEmail.getEditText().getText().toString().trim();
        if(TextUtils.isEmpty(email)){
        }else {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                mEmail.setError("Unesite pravilan email!");
                mEmail.requestFocus();
            } else {
                user.updateEmail(email);
            }
        }


        String pass = mPassword.getEditText().getText().toString().trim();
        if(TextUtils.isEmpty(pass)){
        } else {
            user.updatePassword(pass);
        }


    }


    private void updateUserPicture(String ref) {
        DAOUser dao = new DAOUser();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("profilePicture",ref);

        dao.update(hashMap).addOnFailureListener(err -> {
            Toast.makeText(getContext(),""+err.getMessage(),Toast.LENGTH_LONG).show();
        }).addOnSuccessListener(suc -> {
            Toast.makeText(getContext(),"Uspesno!",Toast.LENGTH_LONG).show();
        });
    }
}