package android.example.com.foodoo.models;

/**
 * Created by ByteTonight on 26.12.2017 using http://www.jsonschema2pojo.org/
 */

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step implements BaseModel, Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("videoURL")
    @Expose
    private String videoURL;
    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailURL;

    private int cardColor;

    private int position = 0;

    public Step() {
    }

    @Override
    public void setCardColor(int color) {
        this.cardColor = color;
    }

    @Override
    public int getCardColor() {
        return cardColor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        // Don't display replacement characters because they catch my eye too quickly
        // Instead let your eyes trick you into reading everything right
        return description.replace("\uFFFD","");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        String returnValue = "";
        if (!TextUtils.isEmpty(videoURL))
            returnValue = videoURL;
        if (!TextUtils.isEmpty(thumbnailURL)) {
            if (thumbnailURL.endsWith("mp4")) {
                returnValue = thumbnailURL;
            }
        }
        return returnValue;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.shortDescription);
        dest.writeString(this.description);
        dest.writeString(this.videoURL);
        dest.writeString(this.thumbnailURL);
        dest.writeInt(this.position);
    }

    protected Step(Parcel in) {
        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
        this.position = in.readInt();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @BindingAdapter("bind:bindingAdapterImgSrc")
    public static void getStepThumbnail(ImageView imageView, String thumbnailURL) {
        Glide.with(imageView.getContext()).load(thumbnailURL).into(imageView);
    }
}