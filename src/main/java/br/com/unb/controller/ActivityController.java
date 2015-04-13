package br.com.unb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;
import br.com.unb.model.Account;
import br.com.unb.model.Activity;
import br.com.unb.model.Project;
import br.com.unb.services.AccountService;
import br.com.unb.services.ActivityService;

@Resource
public class ActivityController {

	@Inject private Result result;
	@Inject private ActivityService activityService;
	@Inject private AccountService accountService;
	
	@Get(value = {"/new/activity"} )
	public void newActivity(Long idAccount) {
		Activity activity = new Activity();
		activity.setAccount(new Account());
		activity.getAccount().setId(idAccount);
		
		result.use(Results.json()).from(activity).recursive().serialize();
	}
	
	@Post(value = {"/save/activity"})
	public Activity save(Activity activity) {
		Long idActivity = activityService.save(activity);
		activity.setId(idActivity);
		
		result.use(Results.json()).from(activity).recursive().serialize();
		
		return activity;
	}
	
	@Get(value = {"/find/activity"})
	public Activity findActivity(long idActivity) {
		Activity activity = activityService.find(idActivity);
		result.use(Results.json()).from(activity).serialize();
		return activity;
	}
	
	@Post("/upload")
	public void upload(final List<UploadedFile> uploadFiles, Activity activity) throws IOException {
		List<String> fileNames = new ArrayList<>();
		
		for (UploadedFile uploadedFile : uploadFiles) {
			System.out.println(uploadedFile.getFileName());
			fileNames.add(uploadedFile.getFileName());
		}
		activityService.saveFiles(uploadFiles);
		
		result.use(Results.http()).body("Upload Files with sucess: " + fileNames.toString());
		result.redirectTo(ActivityController.class);
	}
	
	@Get("/accounts/project")
	public List<Account> buscarAccountsByProject(Long idProject){
		Project project = new Project();
		project.setId(idProject);
		List<Account> listAccount = accountService.list(project);
		result.use(Results.json()).from(listAccount, "accounts").serialize();
		return listAccount;
	}
	
	@Post(value = {"/runCommand"})
	public void runCommand(Activity activity) {
		activityService.runCommand(activity);
	}
	
}
