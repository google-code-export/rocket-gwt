package rocket.remoting.test.comet.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
public class TestCometPayload implements IsSerializable {
    private Date date;
    
    public Date getDate(){
        return this.date;
    }
    
    public void setDate( final Date date ){
        this.date = date;
    }
    
    public String toString(){
        return super.toString() + ", date: " + date;
    }
}
