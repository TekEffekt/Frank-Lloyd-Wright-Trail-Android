package appfactory.edu.uwp.franklloydwrighttrail;

/**
 * Created by zstue_000 on 10/25/2016.
 */
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Example {
        @SerializedName("destination_addresses")
        private List<String> destinationAddresses = new ArrayList<String>();
        @SerializedName("origin_addresses")
        private List<String> originAddresses = new ArrayList<String>();
        @SerializedName("rows")
        private List<Row> rows = new ArrayList<Row>();
        @SerializedName("status")
        private String status;


        /**
         *
         * @return
         * The destinationAddresses
         */
        public List<String> getDestinationAddresses() {
            return destinationAddresses;
        }

        /**
         *
         * @param destinationAddresses
         * The destination_addresses
         */
        public void setDestinationAddresses(List<String> destinationAddresses) {
            this.destinationAddresses = destinationAddresses;
        }

        /**
         *
         * @return
         * The originAddresses
         */
        public List<String> getOriginAddresses() {
            return originAddresses;
        }

        /**
         *
         * @param originAddresses
         * The origin_addresses
         */
        public void setOriginAddresses(List<String> originAddresses) {
            this.originAddresses = originAddresses;
        }

        /**
         *
         * @return
         * The rows
         */
        public List<Row> getRows() {
            return rows;
        }

        /**
         *
         * @param rows
         * The rows
         */
        public void setRows(List<Row> rows) {
            this.rows = rows;
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
