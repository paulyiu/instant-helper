package vtc.project.instanthelper;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Member {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String email;
	
	@Persistent
	private String regid;
	
	public Member(){
		
	}
	
	public Member(String email, String regid){
		this.email = email;
		this.regid = regid;
	}
	
	public static Member find(String email, PersistenceManager pm){
		Query q= pm.newQuery(Member.class);
		q.setFilter("email == emailParam");
		q.declareParameters("String emailParam");
		
		List<Member> result = (List<Member>) q.execute(email);
		if(!result.isEmpty()){
			return result.get(0);
		}
		return null;
	}
	
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getRegid(){
		return regid;
	}
	
	public void setRegid(String regid){
		this.regid = regid;
	}
	
}
