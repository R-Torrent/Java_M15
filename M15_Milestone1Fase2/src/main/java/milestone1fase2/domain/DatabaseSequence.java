package milestone1fase2.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequences")
public class DatabaseSequence {
	
	@Id
	private String id;
	
	private long seq;
	
	public String getId() {
		return id;
	}
	
	public long getSeq() {
		return seq;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setSeq(long seq) {
		this.seq = seq;
	}
	
}
