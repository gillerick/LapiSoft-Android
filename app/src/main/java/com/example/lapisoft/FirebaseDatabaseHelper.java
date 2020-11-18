//package com.example.lapisoft;
//
//import androidx.annotation.NonNull;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FirebaseDatabaseHelper {
//    private FirebaseDatabase mDatabase;
//    private DatabaseReference mReferenceLaptops;
//    private List<Laptop> laptops = new ArrayList<>();
//
//    public interface DataStatus{
//        void DataIsLoaded(List<Laptop> laptops, List<String> keys);
//        void DataIsInserted();
//        void DataIsUpdated();
//        void DataIsDeleted();
//    }
//
//    public FirebaseDatabaseHelper(){
//        mDatabase = FirebaseDatabase.getInstance();
//        mReferenceLaptops = mDatabase.getReference("laptops");
//    }
//
//    public void readLaptops(final DataStatus dataStatus){
//        mReferenceLaptops.addChildEventListener(new ValueEventListener(){
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
//                laptops.clear();
//                List<String> keys = new ArrayList<>();
//                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
//                    keys.add(KeyNode.getKey());
//                    Laptop laptop = keyNode.getValue(Laptop.class);
//                    laptops.add(laptop);
//                dataStatus.DataIsLoaded(laptops, keys);
//            }//                }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });
//    }
//    }
//}
