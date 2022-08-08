package io.renren.modules.check.service;

import io.renren.common.borrow.Borrow;
import io.renren.common.check.Content;
import io.renren.common.check.Creditor;
import io.renren.common.check.Pro;
import io.renren.modules.check.mapper.CheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CheckService {

    @Autowired(required = false)
    private CheckMapper checkMapper;


    public List<Content> list() {
        return checkMapper.list();
    }

    public List<Borrow> borrowList() {
        return checkMapper.blist();
    }

    public List<Borrow> creList(Integer userid) {

        return checkMapper.cerList(userid);
    }

    public Borrow getCreList(Borrow borrow1) {
        return checkMapper.getCreList(borrow1);
    }

    public void addCre(Creditor credutor) {
        checkMapper.addCre(credutor);
    }

    public void updateBorrow(Borrow borrow) {
        checkMapper.updateBorrow(borrow);
    }

    public void updateCre(Creditor credutor) {
        checkMapper.updateCre(credutor);
    }

    public List<Creditor> credutorList() {
        return checkMapper.credutorList();
    }

    public Creditor getPrice(Integer credutorId) {
        return checkMapper.getPrice(credutorId);
    }

    @Transient
    public void addSet(Integer creductorId, Integer uid, BigDecimal qq) {

        //修改借款信息所对应的资方
        Integer borrowId=checkMapper.getBorrowId(creductorId);
        //修改是否转债的状态
        checkMapper.updateZhai(borrowId);
        //查到所对应的资方
        //修改资方
        Pro pro = new Pro();
        Pro pro1 = new Pro();
        pro.setUid(uid);
        Integer pid = checkMapper.getPro(pro);
        pro.setBorrowId(borrowId);
        pro.setId(pid);


        Integer oldpid=checkMapper.getOldPid(pro);
        pro1.setId(oldpid);
        pro1.setQq(qq);
        //旧资方的钱++
        pro.setQq(qq);
        checkMapper.updateOldAddqq(pro1);
        //新资方的钱--
        checkMapper.updateDelqq(pro);

        checkMapper.updatePro(pro);


        //删除掉转让表
        checkMapper.delCre(creductorId);

    }

    public Borrow findBorrowList(Integer borrowId) {
        return checkMapper.findBorrowList(borrowId);
    }
}
