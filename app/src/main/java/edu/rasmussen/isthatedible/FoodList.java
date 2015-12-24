package edu.rasmussen.isthatedible;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Consists of an Arraylist of FoodItems. A Fooditem represents a real world item with an expiration
 * date. These can be added by the user exist in the database and be pinned by the user.
 */
public class FoodList {

    /**
     * An array of food items.
     */
    public ArrayList<FoodItem> ITEMS = new ArrayList<FoodItem>();

    public FoodItem getItemWithName(String name) {
        for (FoodItem item : ITEMS) {
            if (item.toString().equals(name))
                return item;
        }
        return null;
    }

    /**
     * An item representing a single food object
     */
    public static class FoodItem implements Parcelable {
        public final int id;
        public final String content;// name
        public final String details;// expiration
        public final String type;
        public final String notifyPeriod;
        public final String storage; // storage type
        public final boolean hasNotify;
        public final boolean hasLocationServices;
        public boolean isPinned;

        public static final String ITEM_KEY = "item";

        public FoodItem(int id, String content, String details, String type, String notifyPeriod,
                        String storage, int hasNotify, int hasLocation, int isPinned) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.type = type;
            this.notifyPeriod = notifyPeriod;
            this.storage = storage;
            // set the value of hasNotify as long as int is a 0 or 1, default is 1
            this.hasNotify = (hasNotify != 0 && (hasNotify == 1));
            this.hasLocationServices = (hasLocation != 0 && (hasLocation == 1));
            this.isPinned = (isPinned != 0);
        }

        @Override
        public String toString() {
            return content;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(content);
            dest.writeString(details);
            dest.writeString(type);
            dest.writeString(notifyPeriod);
            dest.writeSerializable(storage);
            dest.writeByte((byte) (hasNotify ? 1 : 0));
            dest.writeByte((byte) (hasLocationServices ? 1 : 0));
            dest.writeByte((byte) (isPinned ? 1 : 0));
        }

        protected FoodItem(Parcel in) {
            id = in.readInt();
            content = in.readString();
            details = in.readString();
            type = in.readString();
            notifyPeriod = in.readString();
            storage = in.readString();
            hasNotify = in.readByte() != 0;
            hasLocationServices = in.readByte() != 0;
            isPinned = in.readByte() != 0;
        }

        // Parcelable required creator
        public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
            @Override
            public FoodItem createFromParcel(Parcel in) {
                return new FoodItem(in);
            }

            @Override
            public FoodItem[] newArray(int size) {
                return new FoodItem[size];
            }
        };
    }
}
