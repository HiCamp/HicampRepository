package tw.hicamp.member.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import tw.hicamp.member.model.Member;
import tw.hicamp.member.model.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Member findByEmail(String email) {
		return memberRepository.findByMemberEmail(email);
	}
	
	public Member findByNo(int no) {
		Optional<Member> optional = memberRepository.findById(no);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public void createMember(Member member) {
		member.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
		memberRepository.save(member);
	}
	
	public List<Member> findAllMember(){
		return memberRepository.findAll();
	}
	
	@Transactional
	public Member updateMember(Member member) {
		Optional<Member> optional = memberRepository.findById(member.getMemberNo());
		if (optional.isPresent()) {
			Member memberNewInfo = optional.get();
			memberNewInfo.setMemberName(member.getMemberName());
//			if (passwordEncoder.matches(memberNewInfo.getMemberPassword(), member.getMemberPassword())) {
//				memberNewInfo.setMemberPassword(memberNewInfo.getMemberPassword());
//			} else {
//				memberNewInfo.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
//			}
			if (memberNewInfo.getMemberPassword().equals(member.getMemberPassword())) {
			System.out.println("密碼一致");
				memberNewInfo.setMemberPassword(member.getMemberPassword());
			} else {
			System.out.println("密碼修改");
				memberNewInfo.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
			}
			memberNewInfo.setMemberEmail(member.getMemberEmail());
			memberNewInfo.setMemberPhone(member.getMemberPhone());
			memberNewInfo.setMemberAddress(member.getMemberAddress());
			memberNewInfo.setMemberId(member.getMemberId());
			memberNewInfo.setMemberEmergencyContact(member.getMemberEmergencyContact());
			memberNewInfo.setMemberEmergencyPhone(member.getMemberEmergencyPhone());
			if (member.getMemberGender() != null) {
				memberNewInfo.setMemberGender(member.getMemberGender());
			}
			memberNewInfo.setMemberBirthday(member.getMemberBirthday());
			memberNewInfo.setMemberStatus(member.getMemberStatus());
	        if(member.getMemberPhoto()!= null) {
	        	memberNewInfo.setMemberPhoto(member.getMemberPhoto());
	        }
	        return memberNewInfo;
        }
		
		System.out.println("更新失敗");
		return null;
	}
	
	@Transactional
	public boolean updatePhoto(int memberNo, MultipartFile memberPhoto) {
		Member member = memberRepository.findById(memberNo).get();
		try {
			member.setMemberPhoto(memberPhoto.getBytes());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	@Transactional
	public void checkMemberStatus(Member member) {
		member.setMemberStatus(1);
	}
	
	@Transactional
	public void changeMemberPassword(Member member, String password) {
		member.setMemberPassword(passwordEncoder.encode(password));
	}
	
	public List<Map<Integer, Integer>> findBySignupDate(int year){
	 	return memberRepository.findSignupDateByMemberSignupDate(year);
	}
	
}
