/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpws.security;

/*
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ro.allevo.fintpws.model.UserEntity;
import ro.allevo.fintpws.resources.ApiResource;

public class CustomUserDetailsService implements UserDetailsService {
	@PersistenceContext(unitName="fintpCFG")
	public EntityManager emc;
	
	@PersistenceContext(unitName="fintpDATA")
	public EntityManager emd;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		new UserEntity().setEmc(emc);
		new RolesUtils().setEmc(emc);
		final TypedQuery<UserEntity> query = emc.createNamedQuery(
				"UserEntity.findByUsername", UserEntity.class);

		final java.util.List<UserEntity> results = query.setParameter(
				"username", username).getResultList();

		if (!results.isEmpty()) {
			return results.get(0);
		}
		return null;

	}

}*/
