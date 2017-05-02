package org.moshe.arad.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class BackgammonUserDetails implements UserDetails{

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
	@JsonIgnore
	private Boolean enabled;
	
	@NotBlank
	@Transient
	private String firstName;
	
	@NotBlank
	@Transient
	private String lastName;
	
	@Email
	@Transient
	private String email;

	@Column(name="last_modified_date")
	@NotNull
	@LastModifiedDate
	@JsonIgnore
	private Date lastModifiedDate;
	
	@Column(name="last_modified_by")
	@NotNull
	@LastModifiedBy
	@JsonIgnore
	private String lastModifiedBy;
	
	@Column(name="created_date")
	@NotNull
	@CreatedDate
	@JsonIgnore
	private Date createdDate;
	
	@Column(name="created_by")
	@NotNull
	@CreatedBy
	@JsonIgnore
	private String createdBy;

	public BackgammonUserDetails() {
	}
	
	public BackgammonUserDetails(String userName, String password, Boolean enabled, String firstName, String lastName,
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
				+ enabled + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", lastModifiedDate=" + lastModifiedDate + ", lastModifiedBy=" + lastModifiedBy + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy + "]";
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
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("Backgammon_User");
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return userName;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
}
