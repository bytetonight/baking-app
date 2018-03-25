package android.example.com.foodoo.models;

/**
 * Created by ByteTonight on 26.12.2017 using http://www.jsonschema2pojo.org/
 */

import android.databinding.BindingAdapter;
import android.example.com.foodoo.R;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class Ingredient implements BaseModel, Parcelable
{

    @SerializedName("quantity")
    @Expose
    private Double quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    private int cardColor;
    private int measure_image_resource;

    public final static Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return (new Ingredient[size]);
        }

    };

    protected Ingredient(Parcel in) {
        this.quantity = ((Double) in.readValue((Integer.class.getClassLoader())));
        this.measure = ((String) in.readValue((String.class.getClassLoader())));
        this.ingredient = ((String) in.readValue((String.class.getClassLoader())));
        this.measure_image_resource = ((int) in.readValue((Integer.class.getClassLoader())));
        this.cardColor = ((int) in.readValue((Integer.class.getClassLoader())));
    }

    public Ingredient() {
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getQuantityString() {
    //https://stackoverflow.com/questions/703396/how-to-nicely-format-floating-numbers-to-string
    // -without-unnecessary-decimal-0/4184015#comment75073368_38786041
        return String.format(Locale.getDefault(), (quantity % 1 == 0 ? "%.0f" : "%.1f"), quantity);
    }


    public void setCardColor(int color) {
        this.cardColor = color;
    }


    public int getCardColor() {
        return cardColor;
    }

    public int getCardColorRGB() {
        return cardColor | 0xFF000000;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }


    public void setMeasure(String measure) {
        this.measure = measure;
        switch (measure) {
            case "CUP":
                measure_image_resource = R.drawable.ic_cup_of_tea;
                break;

            case "TBLSP" :
                measure_image_resource = R.drawable.ic_table_spoon_diagonal;
                break;

            case "TSP" :
                measure_image_resource = R.drawable.ic_table_spoon_diagonal;
                break;

            case "OZ" :
                measure_image_resource = R.drawable.ic_flask;
                break;

            case "UNIT" :
                measure_image_resource = 0;
                break;

            default:
                measure_image_resource = R.drawable.ic_kilogram_weight;
                break;
        }
    }

    @BindingAdapter("bind:speeshullSrc")
    public static void setImageDrawable(ImageView view, int resource) {
        view.setImageResource(resource);
    }

    public int getMeasure_image_resource() {
        return measure_image_resource;
    }

    public void setMeasure_image_resource(int measure_image_resource) {
        this.measure_image_resource = measure_image_resource;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(quantity);
        dest.writeValue(measure);
        dest.writeValue(ingredient);
        dest.writeValue(measure_image_resource);
        dest.writeValue(cardColor);
    }

    public int describeContents() {
        return 0;
    }

}
