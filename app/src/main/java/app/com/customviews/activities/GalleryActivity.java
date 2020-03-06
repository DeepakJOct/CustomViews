package app.com.customviews.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import app.com.customviews.R;
import app.com.customviews.helpers.Function;
import app.com.customviews.helpers.MapComparator;
import app.com.customviews.views.CustomFontTextView;
import app.com.customviews.views.CustomImageView;

public class GalleryActivity extends AppCompatActivity {

    static final int REQUEST_PERMISSION_KEY = 1;
    LoadAlbum loadAlbumTask;
    GridView galleryGridView;
    ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        galleryGridView = findViewById(R.id.gallery_grid_view);
        int iDisplayWidth = getResources().getDisplayMetrics().widthPixels;
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = iDisplayWidth / (metrics.densityDpi / 160f);

        if (dp < 360) {
            dp = (dp - 17) / 2;
            float px = Function.convertDpToPixel(dp, getApplicationContext());
            galleryGridView.setColumnWidth(Math.round(px));
        }

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!Function.hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_KEY);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!Function.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        } else {
            loadAlbumTask = new LoadAlbum();
            loadAlbumTask.execute();
        }
    }

    class LoadAlbum extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            albumList.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            String xml = "";
            String album = null;
            String path = null;
            String timeStamp = null;
            String countPhoto = null;
            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_MODIFIED};
            Cursor cursorExternal = getContentResolver().query(uriExternal, projection,
                    "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null,
                    null);
            Cursor cursorInternal = getContentResolver().query(uriInternal, projection,
                    "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null,
                    null);
            Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal});
            while (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                timeStamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
                countPhoto = Function.getCount(getApplicationContext(), album);

                albumList.add(Function.mappingBox(album, path, timeStamp, Function.convertToTime(timeStamp), countPhoto));
            }
            cursor.close();
            Collections.sort(albumList, new MapComparator(Function.KEY_TIMESTAMP, "dsc"));
            //arranging photo album by timestamp descending
            return xml;

        }

        @Override
        protected void onPostExecute(String xml) {
            super.onPostExecute(xml);
            AlbumAdapter adapter = new AlbumAdapter(GalleryActivity.this, albumList);
            galleryGridView.setAdapter(adapter);
            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(GalleryActivity.this, AlbumActivity.class);
                    intent.putExtra("name", albumList.get(+position).get(Function.KEY_ALBUM));
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_KEY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadAlbumTask = new LoadAlbum();
                    loadAlbumTask.execute();
                } else {
                    Toast.makeText(this, "Accept Permissions", Toast.LENGTH_SHORT).show();
                }
        }
    }
}

class AlbumAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public AlbumAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumViewHolder holder;
        if (convertView == null) {
            holder = new AlbumViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_album_row, parent, false);
            holder.galleryImage = convertView.findViewById(R.id.gallery_image);
            holder.gallery_count = convertView.findViewById(R.id.gallery_count);
            holder.gallery_title = convertView.findViewById(R.id.gallery_title);

            convertView.setTag(holder);
        } else {
            holder = (AlbumViewHolder) convertView.getTag();
        }
        holder.gallery_title.setId(position);
        holder.gallery_count.setId(position);
        holder.galleryImage.setId(position);

        HashMap<String, String> song = new HashMap<>();
        song = data.get(position);
        try {
            holder.gallery_title.setText(song.get(Function.KEY_ALBUM));
            holder.gallery_count.setText(song.get(Function.KEY_COUNT));
            Glide.with(activity)
                    .load(new File(song.get(Function.KEY_PATH)))
                    .into(holder.galleryImage);
        } catch (Exception e) {
        }
        return convertView;
    }
}

class AlbumViewHolder {
    CustomImageView galleryImage;
    CustomFontTextView gallery_count, gallery_title;
}