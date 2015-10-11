package datatypes;

public class MongoAdditionalData {

    private String PRODUCT_ID;
    private String NEW_DATA_NAME;
    private MongoAdditionalTypeDTO TYPE;
    private Object VALUE;

    public MongoAdditionalData(String prodID, String DataName, MongoAdditionalTypeDTO type, Object value) {
        this.PRODUCT_ID = prodID;
        this.NEW_DATA_NAME = DataName;
        this.TYPE = type;
        this.VALUE = value;
    }

    // getters
    public String getDataName() {
        return this.NEW_DATA_NAME;
    }

    public String getProductID() {
        return this.PRODUCT_ID;
    }

    public MongoAdditionalTypeDTO getType() {
        return this.TYPE;
    }

    public Object getValue() {
        return this.VALUE;
    }

    // setters
    public void setType(MongoAdditionalTypeDTO t) {
        this.TYPE = t;
    }

    public void setData(Object val) {
        this.VALUE = val;
    }

    public void setDataName(String dn) {
        this.NEW_DATA_NAME = dn;
    }

    public void setProductID(String pid) {
        this.PRODUCT_ID = pid;
    }
    
    @Override
    public String toString(){
        return new String ("PRODUCT_ID " + this.PRODUCT_ID+ " NEW_DATA_NAME " + this.NEW_DATA_NAME + 
                " TYPE " + this.TYPE.toString() + " VALUE" + this.VALUE.toString());
    }
}

