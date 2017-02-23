package appfactory.edu.uwp.franklloydwrighttrail.Apis;

/**
 * Created by zstue_000 on 10/25/2016.
 */
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Distance {


        @SerializedName("text")
        private String text;
        @SerializedName("value")
        private Integer value;


        /**
         *
         * @return
         * The text
         */
        public String getText() {
            return text;
        }

        /**
         *
         * @param text
         * The text
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         *
         * @return
         * The value
         */
        public Integer getValue() {
            return value;
        }

        /**
         *
         * @param value
         * The value
         */
        public void setValue(Integer value) {
            this.value = value;
        }



    }

