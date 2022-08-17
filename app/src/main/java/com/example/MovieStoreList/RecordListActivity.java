package com.example.MovieStoreList;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;

import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;



public class RecordListActivity extends AppCompatActivity {
    ListView mListView;
    ImageView mimageViewIcon;
    ArrayList<Model> mlist;
    RecordListAdapter mAdapter = null;
    private final int IMAGE_PICK_CODE = 1000;
    private final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        final ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Record List");
        mListView = findViewById(R.id.listView);
        mlist = new ArrayList<>();
        mAdapter = new RecordListAdapter(this, R.layout.row, mlist);
        mListView.setAdapter(mAdapter);
        Cursor cursor = Login.mSQLiteHelper.getDate("SELECT * FROM RECORD");
        mlist.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String season = cursor.getString(2);
            String episode = cursor.getString(3);
            byte[] image = cursor.getBlob(4);
            mlist.add(new Model(id, name, season, episode, image));
        }
        mAdapter.notifyDataSetChanged();
        if (mlist.size() == 0) {
            Toast.makeText(this, "No record Found..", Toast.LENGTH_SHORT).show();
        }
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final CharSequence[] items = {"Update", "Delete", "Trial"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(RecordListActivity.this);
                dialog.setTitle("Choose an Action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Cursor c = Login.mSQLiteHelper.getDate("SELECT id FROM RECORD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogUpdate(RecordListActivity.this, arrID.get(position));
                        }
                        if (i == 1) {
                            Cursor c = Login.mSQLiteHelper.getDate("SELECT id FROM RECORD ");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));

                        }if (i == 2){
                            Cursor c = Login.mSQLiteHelper.getDate("SELECT id FROM RECORD ");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogTrial(RecordListActivity.this, actionbar.getDisplayOptions());
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_item){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        return true;
    }

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(RecordListActivity.this);
        dialogDelete.setTitle("Warning");
        dialogDelete.setMessage("Are You Damn Sure To Delete This One");
        dialogDelete.setPositiveButton("Damn Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Login.mSQLiteHelper.deleteData(idRecord);
                    Toast.makeText(RecordListActivity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                updateRecordList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();

    }
    private void showDialogTrial(final Activity Re, final int a){
        final Dialog d = new Dialog(Re);
        d.setContentView(R.layout.activity_login);
        d.setTitle("Login Page");
        final EditText ea = d.findViewById(R.id.ename);
        final EditText eb = d.findViewById(R.id.epassword);
        Button ec = d.findViewById(R.id.ebtn);
        int width = (int) (Re.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (Re.getResources().getDisplayMetrics().heightPixels * 0.4);
        d.getWindow().setLayout(width, height);
        d.show();
    }

    private void showDialogUpdate(final Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Update");
        mimageViewIcon = dialog.findViewById(R.id.imageVIewRecord);
        final EditText edtname = dialog.findViewById(R.id.edtname);
        final EditText edtseason = dialog.findViewById(R.id.edtseason);
        final EditText edtepisode = dialog.findViewById(R.id.edtepisode);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Cursor cursor = Login.mSQLiteHelper.getDate("SELECT * FROM RECORD WHERE id="+position);
        mlist.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            edtname.setText(name);
            String season = cursor.getString(2);
            edtseason.setText(season);
            String episode = cursor.getString(3);
            edtepisode.setText(episode);
            byte[] image = cursor.getBlob(4);
            mimageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(image, 0,image.length));
            mlist.add(new Model(id, name, season, episode, image));
        }
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        mimageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        RecordListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Login.mSQLiteHelper.updateData(
                            edtname.getText().toString().trim(),
                            edtseason.getText().toString().trim(),
                            edtepisode.getText().toString().trim(),
                            Login.imageViewToByte(mimageViewIcon),
                            position
                );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update Error", error.getMessage());

                }
                updateRecordList();
            }
        });
    }

    private void updateRecordList() {
        Cursor cursor = Login.mSQLiteHelper.getDate("SELECT * FROM RECORD");
        mlist.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String season = cursor.getString(2);
            String episode = cursor.getString(3);
            byte[] image = cursor.getBlob(4);
            mlist.add(new Model(id, name, season, episode, image));
        }
        mAdapter.notifyDataSetChanged();
    }


    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            CropImage.activity(data.getData())
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.get().load(resultUri).placeholder(R.drawable.addphoto).fit().into(mimageViewIcon);
            }
        }

    }
}



