# Helix TeamHub Jenkins Notification Plugin [![Build Status](https://jenkins.ci.cloudbees.com/buildStatus/icon?job=plugins/helix-teamhub-plugin)](https://jenkins.ci.cloudbees.com/job/plugins/job/helix-teamhub-plugin/)

Use this plugin to create build events to Helix TeamHub from your Jenkins builds. See the [official documentation](https://wiki.jenkins-ci.org/display/JENKINS/Helix+TeamHub+Plugin) for more information.

## Contributing

To contribute, please utilize Pull Requests as you would in any other project. Development is done on `develop` branch.

### Prerequisites

You'll need [Vagrant](https://www.vagrantup.com/) to get the development environment running. You can simply install it via [Homebrew Cask](http://caskroom.io/) with:

    $ brew cask install vagrant

### Getting started

Start the virtual machine

    $ vagrant up

SSH into the machine

    $ vagrant ssh

Navigate to the shared working directory

    $ cd /vagrant

Build the plugin and start Jenkins

    $ mvn hpi:run

If you are only doing template changes, you can simply hit Enter on the console to reload the context. In other cases you'll need to re-execute the aforementioned command.

You can now access Jenkins from your host machine on [http://10.11.12.100:8080/jenkins](http://10.11.12.100:8080/jenkins).

Finally, follow [the official plugin configuration documentation](https://wiki.jenkins-ci.org/display/JENKINS/Helix+TeamHub+Plugin#HelixTeamHubPlugin-Configuringtheplugin) to set things up.

### Configuring SSH access to Helix TeamHub development environment

For you to be able to configure jobs with SSH URLs, you'll need to generate a new SSH key pair for the user that is running the Jenkins process on your virtual machine.

SSH into the machine and generate a new SSH key

    $ vagrant ssh
    $ ssh-keygen -t rsa

This will generate a private key (`/home/vagrant/.ssh/id_rsa`) and a public key (`/home/vagrant/id_rsa.pub`). Copy the contents of the public key and use it to create a new SSH key to Helix TeamHub. The SSH key should belong to a bot that belongs to the project your configured repository belongs to.

When you're done, your Jenkins job should be able to clone the configured repository successfully via SSH.
