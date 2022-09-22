package jpalogin.login.service;

import jpalogin.login.entity.Member;
import jpalogin.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public void update(Long id, String password) {
        Member member = memberRepository.findById(id).get();
        member.setPassword(password);
    }

    public List findAll() {
        return memberRepository.findAll();
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> user = memberRepository.findUser(member.getLoginId());
        if (!user.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 아이디 입니다");
        }
    }
}
