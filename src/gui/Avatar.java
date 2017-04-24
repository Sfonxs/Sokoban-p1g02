package gui;

public enum Avatar {
    
    Beast("/resources/images/beastG.png"),
    Applefan("/resources/images/appleG.png"),
    Guitarman("/resources/images/guitG.png"),
    Mexicano("/resources/images/mexG.png"),
    Rastaman("/resources/images/rastaG.png");
    
    private String resourceUrl;
    
    Avatar(String resourceUrl){
        this.resourceUrl = resourceUrl;
    }
    
    public String getResourceUrl(){
        return resourceUrl;
    }
}
