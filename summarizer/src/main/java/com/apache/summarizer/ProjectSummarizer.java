package com.apache.summarizer;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Developer;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/*
 * The ProjectSummarizer project is a plugin 
 * that creates a summary of the information 
 * in the pom.xml file inside a Maven project.
 * 
 * @author	Can YILMAZ
 * @since	2022-06-21
 */

//Phase and Goal Determination
@Mojo(name = "summarize", defaultPhase = LifecyclePhase.COMPILE)
public class ProjectSummarizer extends AbstractMojo{
	
	@Parameter(defaultValue = "${project}", required = true)
	private MavenProject project;
	
	public void execute() throws MojoExecutionException, MojoFailureException {

		//Project Details
		String projectInfo =  project.getGroupId() + "." + project.getArtifactId() + "." + project.getVersion();
		List<Dependency> dependencies = project.getDependencies();
		List<Developer> developers = project.getDevelopers();
		List<Plugin> plugins =  project.getBuildPlugins();
		Properties properties = project.getProperties();
		
		File file;
		FileWriter fileWriter;
		int developerCounter = 1;//For Counting Developers

		//Written in Try-Catch Blocks Because It May Get an Error During File Operations
		try {
			file = new File("../summarizer/target/outputFile.txt");
			fileWriter = new FileWriter("../summarizer/target/outputFile.txt");
			
			fileWriter.write("Project info : " + projectInfo + "\n");
			fileWriter.write("Developers : \n" );
			
			for(Developer developer :  developers) {
				fileWriter.write("Developer " + developerCounter + " Name : " + developer.getName() + "\n");
				developerCounter++;
			}
			
			fileWriter.write("Release Date : " + properties.getProperty("release.date") + "\n");
			
			fileWriter.write("Dependencies : \n" );
			for(Dependency dependency : dependencies) { 
				fileWriter.write("Dependency : " + dependency.getGroupId() + "." + dependency.getArtifactId() + "\n");
			}
			
			fileWriter.write("Plugins : \n" );
			for(Plugin plugin : plugins) { 
				fileWriter.write("Plugin : " + plugin.getArtifactId() + "\n");
			}
			
			fileWriter.close();
			
			
		} catch (Exception exception) {
			System.out.println("An unexpected error is occurred.");  
			exception.printStackTrace();  
		}
		
	}

}
