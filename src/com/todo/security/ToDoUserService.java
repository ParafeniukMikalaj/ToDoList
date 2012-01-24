package com.todo.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;

@Service("userDetailsService")
public class ToDoUserService implements UserDetailsService{	
	
	private static ToDoDataProvider provider;

	@Resource(name="dataProviderFactory")
	public void setProviderFactory(DataProviderFactory providerFactory) {
		ToDoUserService.provider = providerFactory.getDataProvider();
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		com.todo.entities.User user = provider.getUserByName(username);
		if(user==null) throw new UsernameNotFoundException("no such user exists");
		return new User(user.getName(), user.getPassword(), authList);
	}

}
