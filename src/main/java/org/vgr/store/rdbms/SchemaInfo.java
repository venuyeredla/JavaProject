package org.vgr.store.rdbms;

import java.util.HashMap;
import java.util.Map;

import org.vgr.store.io.Block;

public class SchemaInfo {
	
    private FileStore fileStore=null;
	private int pageId;
	private String schemaName;
	private String userName;
	private String passWord;
	private int noOfPages;
	private boolean hasIndex;
	private int rootPage;
	private boolean hasTables;
	private Map<String,Integer> tables;
	public SchemaInfo() {
	}
	public SchemaInfo(String schemaName, String userName, String passWord) {
		super();
		this.schemaName = schemaName;
		this.userName = userName;
		this.passWord = passWord;
	 }

	  public SchemaInfo getSchemaInfo() {
		 if(fileStore.isExisted()) {
			 readSchemaInfo();
		 }else {
			 createNew();
		 }
		 return this;
	   }
	
	 public void createNew() {
	    this.setSchemaName(this.schemaName);
	    this.setUserName(this.userName);
	    this.setPassWord(this.passWord);
		this.setPageId(0);
		this.setNoOfPages(0);
		this.setHasIndex(false);
		this.setRootPage(-1);
		this.setHasTables(false);
		persist();
	  }

	  public void persist() {
		Block block = new Block();
		block.write(this.getPageId());
		block.write(this.getSchemaName());
		block.write(this.getUserName());
		block.write(this.getPassWord());
		System.out.println("Total numuber of pages  : " + this.getNoOfPages());
		block.write(this.getNoOfPages());
		byte b = (byte) (this.isHasIndex() ? 1 : 0);
		block.write(b);
		block.write(this.getRootPage());
		byte hasTables=(byte) (this.isHasTables() ? 1 : 0);
		block.write(hasTables);
		if(this.hasTables) {
			block.write((byte)tables.size());
			this.tables.forEach((k,v)->{
				block.write(k);
				block.write(v);
			});
		}
		fileStore.writeBlock(0, block);
		}
	  
	  public void readSchemaInfo() {
		Block block = fileStore.readBlock(0);
		this.setPageId(block.readInt());
		this.setSchemaName(block.readString());
		this.setUserName(block.readString());
		this.setPassWord(block.readString());
		this.setNoOfPages(block.readInt());
		int val = block.readByte();
		boolean hasIndex = val == 0 ? false : true;
		this.setHasIndex(hasIndex);
		this.setRootPage(block.readInt());
		int tablesExist = block.readByte();
		boolean hasTables = tablesExist == 0 ? false : true;
		if(hasTables) {
			byte tablesSize=block.readByte();
			tables=new HashMap<>();
			for(int i=0;i<tablesSize;i++) {
				tables.put(block.readString(), block.readInt());	
			}
		}
	}

	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public int getNoOfPages() {
		return noOfPages;
	}
	public void setNoOfPages(int noOfPages) {
		this.noOfPages = noOfPages;
	}
	public boolean isHasIndex() {
		return hasIndex;
	}
	public void setHasIndex(boolean hasIndex) {
		this.hasIndex = hasIndex;
	}
	public int getRootPage() {
		return rootPage;
	}
	public void setRootPage(int rootPage) {
		this.rootPage = rootPage;
	}
	public int nextPageId() {
	   	return ++noOfPages;
	}
	public boolean isHasTables() {
		return hasTables;
	}
	public void setHasTables(boolean hasTables) {
		this.hasTables = hasTables;
	}
	public Map<String, Integer> getTables() {
		return tables;
	}
	public void setTables(Map<String, Integer> tables) {
		this.tables = tables;
	}
	@Override
	public String toString() {
		return "ScheamaInfo [pageId=" + pageId + ", schemaName=" + schemaName + ", userName=" + userName + ", passWord="
				+ passWord + ", noOfPages=" + noOfPages + ", hasIndex=" + hasIndex + ", rootPage=" + rootPage + "]";
	}
	

}
