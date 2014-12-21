package br.com.unb.services;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Account;
import br.com.unb.model.Activity;
import br.com.unb.repository.ActivityRepository;

@Component
public class ActivityService {

	@Inject private ActivityRepository activityRepository;
	@Inject private AccountService accountService;
	@Inject private GroupService groupService;
	
	public void save(Activity activity) {
		activityRepository.save(activity);
	}

	public List<Activity> list(Account account) {
		return activityRepository.list(account);
	}

	public Activity find(long idActivity) {
		Activity activity = activityRepository.find(idActivity);
		activity.setAccount(accountService.findAccountByActivity(idActivity));
		activity.setGroup(groupService.findGroupByActivity(idActivity));
		return activity;
	}

}
