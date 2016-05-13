package me.l.puppy.dict.impl.stardict;
public class IdxSearcherKeyTree extends IdxSearcher {
    TreeNode root;
    public IdxSearcherKeyTree(IdxReader reader){
        IdxInfo info;
        root=new TreeNode("#");
        while ((info = reader.next()) != null) {
           insert(info);
        }
    }

    private void insert(IdxInfo idx){
        int startIdxOfword = 0;
        TreeNode node=root;
        String word4insert="#"+idx.word;
        int m=match(word4insert,startIdxOfword,node);
        while (m!=0){
            if(m+1==node.word.length()){
                startIdxOfword+=m;
                node=node.child;
            }else{
                //split current node into two nodes
                String part1=node.word.substring(0,m);
                String part2=node.word.substring(m);
                node.word=part1;
                TreeNode child=new TreeNode(part2);
                child.info=node.info;
                //ignore sibling's order
                child.sibling=node.child;
                node.child=child;
                break;
            }
        }
        String restPart=word4insert.substring(startIdxOfword);
        TreeNode n=new TreeNode(restPart,idx,null,null);
        n.sibling=node.child;
        node.child=n;
    }

    /**
    * 
    */
    private int match(String word,int startIdxOfword,TreeNode node){
        int lenOfWord=word.length();
        int len=node.word.length();
        int i=0;
        for(i=0;i<len && i+startIdxOfword < lenOfWord ;i++){
            if(word.chatAt(i+startIdxOfword)!=node.word.charAt(i)){
                return i;
            }
        }
        return i;
    }
    //Is s1[idxOfs1:...] starts with s2
    private boolean startsWith(String s1,String s2,int idxOfS1){
        int restOfs1=s1.length()-idxOfS1;
        int lenOfs2=s2.length();
        if(restOfs1<lenOfs2){
            return false;
        }
        for(var i=0;i<lenOfs2;i++){
            if(s1.charAt(i+idxOfS1)!=s2.child(i)){
                return false;
            }
        }
        return true;
    }

    public  IdxInfo search(String string){
        string="#"+string;
        int startIdxOfword=0;
        int len=string.length();
        TreeNode node = root;
        while (startIdxOfword<len){
            while(!startsWith(string,node.word,startIdxOfword))
            {
                node=node.sibling;
                if(node==null){
                    return null;
                }
            }
            startIdxOfword+=node.word.length();
            node=node.child;
        }
        return node.info;
    }
    private List<IdxInfo> searchStartsWith(String string,int maxResults){
        List<IdxInfo> idxInfos=new ArrayList<IdxInfo>();
        
    }
    public  List<IdxInfo> search(String string,SearchStrategy strategy,int maxResults){

    }
}
class TreeNode{
    public String word;
    public IdxInfo info;
    public TreeNode child;
    public TreeNode sibling;
    public TreeNode(String word,IdxInfo info,TreeNode child,TreeNode sibling){
        this.word=word;
        this.info=info;
        this.child=child;
        this.sibling=sibling;
    }
    public TreeNode(String word){
        this(word,null,null,null);
    }
}