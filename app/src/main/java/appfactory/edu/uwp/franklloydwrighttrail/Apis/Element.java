package appfactory.edu.uwp.franklloydwrighttrail.Apis;

/**
 * Created by zstue_000 on 10/25/2016.
 */
import com.google.gson.annotations.SerializedName;

public class Element {

        @SerializedName("distance")
        private Distance distance;
        @SerializedName("duration")
        private Duration duration;
        @SerializedName("status")
        private String status;


        /**
         *
         * @return
         * The distance
         */
        public Distance getDistance() {
            return distance;
        }

        /**
         *
         * @param distance
         * The distance
         */
        public void setDistance(Distance distance) {
            this.distance = distance;
        }

        /**
         *
         * @return
         * The duration
         */
        public Duration getDuration() {
            return duration;
        }

        /**
         *
         * @param duration
         * The duration
         */
        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        /**
         *
         * @return
         * The status
         */
        public String getStatus() {
            return status;
        }

        /**
         *
         * @param status
         * The status
         */
        public void setStatus(String status) {
            this.status = status;
        }



    }

