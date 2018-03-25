package android.example.com.foodoo.models;

/**
 * Created by ByteTonight on 26.12.2017 using http://www.jsonschema2pojo.org/
 */

import android.content.Context;
import android.databinding.BindingAdapter;
import android.example.com.foodoo.R;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe implements BaseModel, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    private int cardColor;

    public final static Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }

    };

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        // For some f***ing reason the line below does not work anymore
        //in.readList(this.ingredients, (android.example.com.foodoo.models.Ingredient.class.getClassLoader()));
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        //in.readList(this.steps, (android.example.com.foodoo.models.Step.class.getClassLoader()));
        steps = in.createTypedArrayList(Step.CREATOR);
        this.servings = in.readInt();
        this.image =  in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int describeContents() {
        return 0;
    }

    public void setCardColor(int color) {
        this.cardColor = color;
    }

    public int getCardColor() {
        return cardColor;
    }

    @BindingAdapter("bind:getRecipeImage")
    public static void getRecipeImage(ImageView imageView, String imageUri) {

        final Context context = imageView.getContext();
        Glide.with(context)
                .load( imageUri)
                .asBitmap()
                .dontAnimate()
                //.centerCrop()
                //.thumbnail(.1f)
                .placeholder(R.drawable.cupcake)
                .into(imageView);

    }

}
