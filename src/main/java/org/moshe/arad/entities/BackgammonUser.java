package org.moshe.arad.entities;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class BackgammonUser implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userId;
	
	@Column(name="username")
	@NotBlank
	private String userName;
	
	@NotBlank
	private String password;
	
	@NotNull
	private Boolean enabled;

	@Column(name="last_modified_date")
	@NotNull
	@LastModifiedDate
	private Date lastModifiedDate;
	
	@Column(name="last_modified_by")
	@NotNull
	@LastModifiedBy
	private String lastModifiedBy;
	
	@Column(name="created_date")
	@NotNull
	@CreatedDate
	private Date createdDate;
	
	@Column(name="created_by")
	@NotNull
	@CreatedBy
	private String createdBy;

	public BackgammonUser() {
	}
	
	public BackgammonUser(String userName, String password, Boolean enabled, String firstName, String lastName,
			String email, Date lastModifiedDate, String lastModifiedBy, Date createdDate, String createdBy) {
		super();
		this.userName = userName;
		this.password = password;
		this.enabled = enabled;
		this.lastModifiedDate = lastModifiedDate;
		this.lastModifiedBy = lastModifiedBy;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "BackgammonUser [userId=" + userId + ", userName=" + userName + ", password=" + password + ", enabled="
				+ enabled + ", lastModifiedDate=" + lastModifiedDate + ", lastModifiedBy=" + lastModifiedBy
				+ ", createdDate=" + createdDate + ", createdBy=" + createdBy + "]";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createDate) {
		this.createdDate = createDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("Backgammon_User");
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
