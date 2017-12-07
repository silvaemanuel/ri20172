public class Doc {
    public String id;
    public int number;
    public Doc next;
    public Doc previous;

    
    public Doc(String id, int number, Doc next, Doc previous){
        this.id = id;
        this.number = number;
        this.previous = previous;
        this.next = next;
    }
    
    public Doc(){
        this.id = null;
        this.number = 0;
        this.previous = null;
        this.next = null;
    }
    
    public int getNumber(){
        return number;
    }
    
    public void setNumber(int number){
        this.number = number;
    }
    
    public int getId(){
        return Integer.parseInt(id);
    }
    
    public void setId(String id){
        this.id = id;
    }
    
    public Doc getAt(Doc init, int index){
    	Doc current = init;
        int count = 0;
        while (current != null){
           if (count == index){
              return current;
           }
           count++;
           current = current.next;
        }
        return null;              
    }
}
