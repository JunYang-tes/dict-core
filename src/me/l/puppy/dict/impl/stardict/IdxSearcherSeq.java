package me.l.puppy.dict.impl.stardict;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import me.l.puppy.dict.core.SearchStrategy;
class IdxSearcherSeq  extends IdxSearcher{
    List<IdxInfo> idxInfos;
     
    public IdxSearcherSeq(IdxReader reader) {
    	super(reader);
        idxInfos = new ArrayList<IdxInfo>();
        IdxInfo info;
        while ((info = reader.next()) != null) {
            idxInfos.add(info);
        }
    }

    public IdxInfo search(String string) {
        IdxInfo idx = new IdxInfo();
        idx.word = string;
        IdxInfo tmp;
        int l = 0, h = idxInfos.size() - 1;
        int m = (h + l) / 2;
        int comp = 0;
        while (l <= h) {
            m = (h + l) / 2;
            tmp = idxInfos.get(m);
            comp = idx.compareTo(tmp);
            if (comp == 0) {
                return tmp;
            } else if (comp > 0) {
                l = m + 1;
            } else {
                h = m - 1;
            }
        }
        return null;
    }
    private SearchResult search_(String string,int low,int high){
        if(low>high){
            return null;
        }
        SearchResult sr=new SearchResult();
        int m=(high+low) / 2;
        int comp=0;
        IdxInfo tmp;
        while(low <= high){
            m=(high+low)/2;
            tmp=idxInfos.get(m);
            if(tmp.word.startsWith(string)){
                sr.info=tmp;
                sr.low=low;
                sr.high=high;
                sr.mid=m;
                return sr;
            }
            comp=tmp.word.compareTo(string);
            if(comp>0){
                low=m+1;
            }else{
                high=m-1;
            }
        }
        return null;
    }
    private List<IdxInfo> searchStartsWith(String string,int maxResults){
        List<IdxInfo> infos=new ArrayList<IdxInfo>();
        SearchResult sr =this.search_(string,0,idxInfos.size());
        Stack<SearchResult> srStack =new Stack<SearchResult>();
        if(sr!=null)
            srStack.push(sr);
        while(maxResults>0 && !srStack.empty()){
            sr=srStack.pop();
            maxResults--;
            infos.add(sr.info);
            SearchResult tmp=this.search_(string,sr.low,sr.mid-1);
            if(tmp!=null)
                srStack.push(tmp);
            tmp=this.search_(string,sr.mid+1,sr.high);
            if(tmp!=null)
                srStack.push(tmp);  
        }
        return infos;
    }

    public List<IdxInfo> search(String string,SearchStrategy strategy,int maxResults) {
        if(strategy==SearchStrategy.StartsWith){
            return this.searchStartsWith(string,maxResults);
        } else if (strategy == SearchStrategy.Contains || strategy == SearchStrategy.EndsWith){
            int idx=0;
            List<IdxInfo> infos=new ArrayList<IdxInfo>(maxResults);
            IdxInfo tmp=null;
            int size=idxInfos.size();
            while(maxResults-->0 && idx<size){
                tmp=idxInfos.get(idx++);
                if(strategy==SearchStrategy.Contains && tmp.word.contains(string)){
                    infos.add(tmp);
                }else if(strategy==SearchStrategy.EndsWith && tmp.word.endsWith(string)){
                    infos.add(tmp);
                }
            }
        } 
        return new ArrayList<IdxInfo> ();
    }
    
    public List<IdxInfo> getAll(){
        return new ArrayList<IdxInfo>(idxInfos);
    }
    class SearchResult{
        public int low;
        public int high;
        public int mid;
        public IdxInfo info;
    }

}