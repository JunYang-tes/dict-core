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

    public  IdxInfo search(String string){

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