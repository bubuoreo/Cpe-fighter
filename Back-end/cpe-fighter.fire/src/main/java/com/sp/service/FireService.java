package com.sp.service;


import java.util.*;

import com.DTO.FireDTO;
import com.sp.model.Fire;
import com.sp.repository.FireRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FireService {
   
	@Autowired
    FireRepository fireRepository;
	
	private FireDTO FireToFireDTO(Fire fire){
		FireDTO fireDto = new FireDTO(fire.getId(),fire.getType(),fire.getIntensity(),fire.getRange(),fire.getLon(),fire.getLat());
		return fireDto;
	}
	
	/*
	private Fire FireDTOToFire(FireDTO fireDTO){
		Fire fire = new Fire(fireDTO.getId(),fireDTO.getType(),fireDTO.getIntensity(),fireDTO.getRange(),fireDTO.getLon(),fireDTO.getLat());
		return fire;
	}
	*/
	
    public FireDTO getFireById(int id) {
		Optional<Fire> fire = fireRepository.findById(id);
		if (fire.isPresent()) {
			return this.FireToFireDTO(fire.get());
		} else {
			return null;
		}
    }
    public Iterable<FireDTO> getAllFires() {
		Iterable<Fire> fires = fireRepository.findAll();
		List<FireDTO> firesDTO = new ArrayList<FireDTO>();
		for(Fire fire : fires){
			firesDTO.add(this.FireToFireDTO(fire));
		}
		return firesDTO;
	}

	/*
    public UserDTO login(LoginRequest loginRequest) {
        User user = this.userRepository.findByEmail(loginRequest.getEmail());
        if (user != null) {
            if (user.getPassword().equals(loginRequest.getPassword())) {
                return this.UserToUserDTO(user);
            }
        }
        throw new ResponseStatusException(null);
    }

    public UserDTO register(RegisterRequest registerRequest) {

        UserDTO userDTO = new UserDTO(registerRequest.getEmail(), registerRequest.getName(), registerRequest.getSurname(), registerRequest.getPassword(), registerRequest.getSold(),generateRandomArray(5));
        User user_register = this.userRepository.save((this.UserDTOToUser(userDTO)));
        return this.UserToUserDTO(user_register);
    }
    
    
    public List<Integer> generateRandomArray(int n){
        ArrayList<Integer> list = new ArrayList<Integer>(n);
        Random random = new Random();
        
        for (int i = 0; i < n; i++)
        {
            list.add(random.nextInt(100));
        }
       return list;
    } 
    
    public UserDTO update(UserDTO userDTO) {
    	User user = this.UserDTOToUser(userDTO);
		User userupdate = this.userRepository.save(user);
		return this.UserToUserDTO(userupdate);
    }
    
    public Iterable<UserDTO> getAllUsers() {
		Iterable<User> users = userRepository.findAll();
		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		for(User user : users){
			usersDTO.add(this.UserToUserDTO(user));
		}
		return usersDTO;
	}
	
    
    public UserDTO getUserById(int id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return this.UserToUserDTO(user.get());
		} else {
			return null;
		}
	}

*/
}
	
	