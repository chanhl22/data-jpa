package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EntityManagerTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @DisplayName("1차 캐시 미사용")
    @Test
    public void entityManagerNotUsed() {
        //given
        String username = "chanhl22";
        Member member = new Member(username, 22);
        memberRepository.save(member);
        memberRepository.findByUsername(username);

        //when
        Member findMember = memberRepository.findMemberByUsername(username);

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @DisplayName("1차 캐시 사용")
    @Test
    public void entityManagerUsed() {
        //given
        String username = "chanhl23";
        Member member = new Member(username, 23);
        Member savedMember = memberRepository.save(member);
        memberRepository.findByUsername(username);

        //when
        Optional<Member> findMembers = memberRepository.findById(savedMember.getId());

        //then
        assertThat(findMembers.get()).isEqualTo(member);
    }

}