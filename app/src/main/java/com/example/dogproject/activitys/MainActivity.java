package com.example.dogproject.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dogproject.DialogScreen;
import com.example.dogproject.NetworkService;
import com.example.dogproject.R;
import com.example.dogproject.adapters.BreedAdapter;
import com.example.dogproject.adapters.FavouriteAdapter;
import com.example.dogproject.data.DbHelper;
import com.example.dogproject.data.FavouriteDogs;
import com.example.dogproject.pojo.Dogs;
import com.example.dogproject.pojo.Message;
import com.example.dogproject.pojo.Value;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private androidx.appcompat.widget.Toolbar toolbarBreed;
    private TextView titleBreed;

    private RecyclerView breeds;
    private BreedAdapter breedAdapter;
    private FavouriteAdapter favouriteAdapter;

    private ArrayList<Dogs> breedsList;
    private ArrayList<Dogs> breedsFavouriteList;

    @Override
    protected void onStart() {
        super.onStart();
            DbHelper mDbHelper = new DbHelper(this);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {
                    FavouriteDogs.FavouriteImages._ID,
                    FavouriteDogs.FavouriteImages.COLUMN_NAME,
                    FavouriteDogs.FavouriteImages.COLUMN_IMAGES};

            Cursor cursor = db.query(
                    FavouriteDogs.FavouriteImages.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null);

            int nameColumnIndex = cursor.getColumnIndex(FavouriteDogs.FavouriteImages.COLUMN_NAME);
            int imageColumnIndex = cursor.getColumnIndex(FavouriteDogs.FavouriteImages.COLUMN_IMAGES);
            breedsFavouriteList.clear();
            while (cursor.moveToNext()){
                String currentBreed = cursor.getString(nameColumnIndex);
                String currentImages = cursor.getString(imageColumnIndex);
                ArrayList<String> arr = new ArrayList<>(Arrays.asList(currentImages.split(", ")));
                breedsFavouriteList.add(new Dogs(currentBreed, arr.size(), arr));
            }
            favouriteAdapter.setItems(breedsFavouriteList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navig_view);
        breeds = findViewById(R.id.dogs_breed_list);
        breedsFavouriteList = new ArrayList<>();

        toolbarBreed = findViewById(R.id.toolbar_actionbar);
        titleBreed = findViewById(R.id.title);
        titleBreed.setText("Breeds");
        setSupportActionBar(toolbarBreed);

        initRecyclerView();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.list:
                        titleBreed.setText("Breeds");
                        breeds.setAdapter(breedAdapter);
                        breedAdapter.setItems(breedsList);
                        return true;
                    case R.id.favourite:
                            titleBreed.setText("Favourites");
                            breeds.setAdapter(favouriteAdapter);
                            favouriteAdapter.setItems(breedsFavouriteList);
                            return true;
                    }
                    return false;
            }
        });

    }
    private void initRecyclerView() {
        breeds.setLayoutManager(new LinearLayoutManager(this));
        BreedAdapter.OnBreedClickListener onBreedClickListener = new BreedAdapter.OnBreedClickListener() {
            @Override
            public void onBreedClick(Dogs dog) {
                if(dog.getCountSubBreed() != 0){
                    Intent intent = new Intent(MainActivity.this, SubBreedsActivity.class);
                    intent.putExtra(SubBreedsActivity.DOG_BREED, dog.getBreed());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                    intent.putExtra(ImageActivity.BREED, dog.getBreed());
                    intent.putExtra(ImageActivity.SUB_BREED, "");
                    startActivity(intent);
                }

            }
        };
        FavouriteAdapter.OnFavouriteClickListener onFavouriteClickListener = new FavouriteAdapter.OnFavouriteClickListener() {
            @Override
            public void onFavouriteBreedClick(String breed, ArrayList<String> imageDogs) {
                Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
                intent.putExtra(FavouriteActivity.FAVOURITE, imageDogs);
                intent.putExtra(FavouriteActivity.PICTURES, breed);
                startActivity(intent);
            }
        };

        breedAdapter = new BreedAdapter(onBreedClickListener);
        favouriteAdapter = new FavouriteAdapter(onFavouriteClickListener);
        breeds.setAdapter(breedAdapter);

        NetworkService.getInstance()
                .getBreeds()
                .getValue()
                .enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.body().getStatus().equals("success"))
                    breedsList = initAllBreeds(response.body().getMessage());
                breedAdapter.setItems(breedsList);
            }
            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                AlertDialog dialog = DialogScreen.getDialog(MainActivity.this);
                dialog.show();
            }
        });
    }



    private ArrayList<Dogs> initAllBreeds(Message message){

        ArrayList<Dogs> dogs = new ArrayList<>();

        dogs.add(new Dogs("Affenpinscher", message.getAffenpinscher().size(),message.getAffenpinscher()));
        dogs.add(new Dogs("African", message.getAfrican().size(),message.getAfrican()));
        dogs.add(new Dogs("Airedale", message.getAiredale().size(),message.getAiredale()));
        dogs.add(new Dogs("Akita", message.getAkita().size(),message.getAkita()));
        dogs.add(new Dogs("Appenzeller", message.getAppenzeller().size(),message.getAppenzeller()));
        dogs.add(new Dogs("Australian", message.getAustralian().size(),message.getAustralian()));
        dogs.add(new Dogs("Basenji", message.getBasenji().size(),message.getBasenji()));
        dogs.add(new Dogs("Beagle", message.getBeagle().size(),message.getBeagle()));
        dogs.add(new Dogs("Bluetick", message.getBluetick().size(),message.getBluetick()));
        dogs.add(new Dogs("Borzoi", message.getBorzoi().size(),message.getBorzoi()));
        dogs.add(new Dogs("Bouvier", message.getBouvier().size(),message.getBouvier()));
        dogs.add(new Dogs("Boxer", message.getBoxer().size(),message.getBoxer()));
        dogs.add(new Dogs("Brabancon", message.getBrabancon().size(),message.getBrabancon()));
        dogs.add(new Dogs("Briard", message.getBriard().size(),message.getBriard()));
        dogs.add(new Dogs("Buhund", message.getBuhund().size(),message.getBuhund()));
        dogs.add(new Dogs("Bulldog", message.getBulldog().size(),message.getBulldog()));
        dogs.add(new Dogs("Bullterrier", message.getBullterrier().size(),message.getBullterrier()));
        dogs.add(new Dogs("Cairn", message.getCairn().size(),message.getCairn()));
        dogs.add(new Dogs("Cattledog", message.getCattledog().size(),message.getCattledog()));
        dogs.add(new Dogs("Chihuahua", message.getChihuahua().size(),message.getChihuahua()));
        dogs.add(new Dogs("Chow", message.getChow().size(),message.getChow()));
        dogs.add(new Dogs("Clumber", message.getClumber().size(),message.getClumber()));
        dogs.add(new Dogs("Cockapoo", message.getCockapoo().size(),message.getCockapoo()));
        dogs.add(new Dogs("Collie", message.getCollie().size(),message.getCollie()));
        dogs.add(new Dogs("Coonhound", message.getCoonhound().size(),message.getCoonhound()));
        dogs.add(new Dogs("Corgi", message.getCorgi().size(),message.getCorgi()));
        dogs.add(new Dogs("Cotondetulear", message.getCotondetulear().size(),message.getCotondetulear()));
        dogs.add(new Dogs("Dachshund", message.getDachshund().size(),message.getDachshund()));
        dogs.add(new Dogs("Dalmatian", message.getDalmatian().size(),message.getDalmatian()));
        dogs.add(new Dogs("Dane", message.getDane().size(),message.getDane()));
        dogs.add(new Dogs("Deerhound", message.getDeerhound().size(),message.getDeerhound()));
        dogs.add(new Dogs("Dhole", message.getDhole().size(),message.getDhole()));
        dogs.add(new Dogs("Dingo", message.getDingo().size(),message.getDingo()));
        dogs.add(new Dogs("Doberman", message.getDoberman().size(),message.getDoberman()));
        dogs.add(new Dogs("Elkhound", message.getElkhound().size(),message.getElkhound()));
        dogs.add(new Dogs("Entlebucher", message.getEntlebucher().size(),message.getEntlebucher()));
        dogs.add(new Dogs("Eskimo", message.getEskimo().size(),message.getEskimo()));
        dogs.add(new Dogs("Finnish", message.getFinnish().size(),message.getFinnish()));
        dogs.add(new Dogs("Frise", message.getFrise().size(),message.getFrise()));
        dogs.add(new Dogs("Germanshepherd", message.getGermanshepherd().size(),message.getGermanshepherd()));
        dogs.add(new Dogs("Greyhound", message.getGreyhound().size(),message.getGreyhound()));
        dogs.add(new Dogs("Groenendael", message.getGroenendael().size(),message.getGroenendael()));
        dogs.add(new Dogs("Havanese", message.getHavanese().size(),message.getHavanese()));
        dogs.add(new Dogs("Hound", message.getHound().size(),message.getHound()));
        dogs.add(new Dogs("Husky", message.getHusky().size(),message.getHusky()));
        dogs.add(new Dogs("Keeshond", message.getKeeshond().size(),message.getKeeshond()));
        dogs.add(new Dogs("Kelpie", message.getKelpie().size(),message.getKelpie()));
        dogs.add(new Dogs("Komondor", message.getKomondor().size(),message.getKomondor()));
        dogs.add(new Dogs("Kuvasz", message.getKuvasz().size(),message.getKuvasz()));
        dogs.add(new Dogs("Labrador", message.getLabrador().size(),message.getLabrador()));
        dogs.add(new Dogs("Leonberg", message.getLeonberg().size(),message.getLeonberg()));
        dogs.add(new Dogs("Lhasa", message.getLhasa().size(),message.getLhasa()));
        dogs.add(new Dogs("Malamute", message.getMalamute().size(),message.getMalamute()));
        dogs.add(new Dogs("Malinois", message.getMalinois().size(),message.getMalinois()));
        dogs.add(new Dogs("Maltese", message.getMaltese().size(),message.getMaltese()));
        dogs.add(new Dogs("Mastiff", message.getMastiff().size(),message.getMastiff()));
        dogs.add(new Dogs("Mexicanhairless", message.getMexicanhairless().size(),message.getMexicanhairless()));
        dogs.add(new Dogs("Mix", message.getMix().size(),message.getMix()));
        dogs.add(new Dogs("Mountain", message.getMountain().size(),message.getMountain()));
        dogs.add(new Dogs("Newfoundland", message.getNewfoundland().size(),message.getNewfoundland()));
        dogs.add(new Dogs("Otterhound", message.getOtterhound().size(),message.getOtterhound()));
        dogs.add(new Dogs("Ovcharka", message.getOvcharka().size(),message.getOvcharka()));
        dogs.add(new Dogs("Papillon", message.getPapillon().size(),message.getPapillon()));
        dogs.add(new Dogs("Pekinese", message.getPekinese().size(),message.getPekinese()));
        dogs.add(new Dogs("Pembroke", message.getPembroke().size(),message.getPembroke()));
        dogs.add(new Dogs("Pinscher", message.getPinscher().size(),message.getPinscher()));
        dogs.add(new Dogs("Pitbull", message.getPitbull().size(),message.getPitbull()));
        dogs.add(new Dogs("Pointer", message.getPointer().size(),message.getPointer()));
        dogs.add(new Dogs("Pomeranian", message.getPomeranian().size(),message.getPomeranian()));
        dogs.add(new Dogs("Poodle", message.getPoodle().size(),message.getPoodle()));
        dogs.add(new Dogs("Pug", message.getPug().size(),message.getPug()));
        dogs.add(new Dogs("Puggle", message.getPuggle().size(),message.getPuggle()));
        dogs.add(new Dogs("Pyrenees", message.getPyrenees().size(),message.getPyrenees()));
        dogs.add(new Dogs("Redbone", message.getRedbone().size(),message.getRedbone()));
        dogs.add(new Dogs("Retriever", message.getRetriever().size(),message.getRetriever()));
        dogs.add(new Dogs("Ridgeback", message.getRidgeback().size(),message.getRidgeback()));
        dogs.add(new Dogs("Rottweiler", message.getRottweiler().size(),message.getRottweiler()));
        dogs.add(new Dogs("Saluki", message.getSaluki().size(),message.getSaluki()));
        dogs.add(new Dogs("Samoyed", message.getSamoyed().size(),message.getSamoyed()));
        dogs.add(new Dogs("Schipperke", message.getSchipperke().size(),message.getSchipperke()));
        dogs.add(new Dogs("Schnauzer", message.getSchnauzer().size(),message.getSchnauzer()));
        dogs.add(new Dogs("Setter", message.getSetter().size(),message.getSetter()));
        dogs.add(new Dogs("Sheepdog", message.getSheepdog().size(),message.getSheepdog()));
        dogs.add(new Dogs("Shiba", message.getShiba().size(),message.getShiba()));
        dogs.add(new Dogs("Shihtzu", message.getShihtzu().size(),message.getShihtzu()));
        dogs.add(new Dogs("Spaniel", message.getSpaniel().size(),message.getSpaniel()));
        dogs.add(new Dogs("Springer", message.getSpringer().size(),message.getSpringer()));
        dogs.add(new Dogs("Stbernard", message.getStbernard().size(),message.getStbernard()));
        dogs.add(new Dogs("Terrier", message.getTerrier().size(),message.getTerrier()));
        dogs.add(new Dogs("Vizsla", message.getVizsla().size(),message.getVizsla()));
        dogs.add(new Dogs("Waterdog", message.getWaterdog().size(),message.getWaterdog()));
        dogs.add(new Dogs("Weimaraner", message.getWeimaraner().size(),message.getWeimaraner()));
        dogs.add(new Dogs("Whippet", message.getWhippet().size(),message.getWhippet()));
        dogs.add(new Dogs("Wolfhound", message.getWolfhound().size(),message.getWolfhound()));

        return dogs;
    }
}