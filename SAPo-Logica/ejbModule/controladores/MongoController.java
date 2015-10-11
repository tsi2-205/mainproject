package controladores;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import interfaces.IMongoController;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;



public class MongoController implements IMongoController {
    
    private static String dbName = "SAPOMONGO";
     private static MongoController instance;
     private MongoDatabase db;
     
 
    private MongoController(){
        MongoClient mongoClient = new MongoClient();
        this.db = mongoClient.getDatabase( this.dbName );
    }

    public MongoDatabase getCurrentDB (){
        return this.db;
    }
    
    public List<datatypes.MongoAdditionalData> getAdditionalDataFromProduct (String productid){
        
         FindIterable<Document> iterable = this.db.getCollection("ADITIONAL_STORE_DATA").find( new Document("PRODUCT_ID", productid ) );
        
         final List retlist = new ArrayList<datatypes.MongoAdditionalData>();
         iterable.forEach(new Block<Document>() {
             @Override
             public void apply(final Document document) {
                 
                 Object obj = null;
                 if  ( document.getString("TYPE").equals(datatypes.MongoAdditionalTypeDTO.STRING.toString())   || 
                     document.getString("TYPE").equals(datatypes.MongoAdditionalTypeDTO.VIDEO.toString()) || 
                     document.getString("TYPE").equals(datatypes.MongoAdditionalTypeDTO.IMAGE.toString()) ){

                     obj = new String (document.getString("VALUE"));
                     
                 }
                 
                 if  ( document.getString("TYPE").equals(datatypes.MongoAdditionalTypeDTO.INT.toString()) )
                     obj = new Integer (document.getString("VALUE"));
                 if  ( document.getString("TYPE").equals(datatypes.MongoAdditionalTypeDTO.DOUBLE.toString()) )
                     obj = new Double (document.getString("VALUE"));
                 if  ( document.getString("TYPE").equals(datatypes.MongoAdditionalTypeDTO.CHAR.toString()) )
                     obj = new Character (document.getString("VALUE").charAt(0));
                 
                 datatypes.MongoAdditionalTypeDTO tp;
                 switch (document.getString("TYPE")) {
                     case "CHAR":  tp= datatypes.MongoAdditionalTypeDTO.CHAR;
                              break;
                     case "STRING":  tp= datatypes.MongoAdditionalTypeDTO.STRING;
                              break;
                     case "INT":  tp= datatypes.MongoAdditionalTypeDTO.INT;
                              break;
                     case "DOUBLE":  tp= datatypes.MongoAdditionalTypeDTO.DOUBLE;
                              break;
                     case "IMAGE":  tp= datatypes.MongoAdditionalTypeDTO.IMAGE;
                              break;
                     default:  tp= datatypes.MongoAdditionalTypeDTO.VIDEO;
                              break;
                 }
                 
                 retlist.add( new datatypes.MongoAdditionalData ( document.getString( "PRODUCT_ID" ), document.getString( "NEW_DATA_NAME" ), tp, obj ) );
             }// apply
        }); 
        return retlist;
    }
    
    
     public void addAdditionalDataToProduct ( List<datatypes.MongoAdditionalData> listData ){
         for (datatypes.MongoAdditionalData listData1 : listData) {
             Document doc = new Document();
             doc.append("PRODUCT_ID", listData1.getProductID());
             doc.append("NEW_DATA_NAME", listData1.getDataName() );
             doc.append("TYPE", listData1.getType().toString());

             switch ( listData1.getType().toString() ) {
                 case "STRING": 
                          doc.append("VALUE", (String)listData1.getValue() );
                          break;
                 case "INT":
                           doc.append("VALUE", (Integer)listData1.getValue() );
                          break;
                 case "DOUBLE": 
                          doc.append("VALUE", (Double)listData1.getValue() );
                          break;
                 case "CHAR":  
                          doc.append("VALUE", (Character)listData1.getValue() );
                          break;
                 case "IMAGE":
                          doc.append("VALUE", (String)listData1.getValue() );
                          break;
                 case "VIDEO": 
                          doc.append("VALUE", (String)listData1.getValue() );
                          break;
                 default:
                          break;
             }
             System.out.println(doc.toString());
             this.db.getCollection("ADITIONAL_STORE_DATA").insertOne( doc );
         }
     }
}

