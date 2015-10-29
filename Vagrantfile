# -*- mode: ruby -*-
# vi: set ft=ruby :
Vagrant.configure(2) do |config|

  config.vm.box = "ubuntu/trusty64"
  config.vm.network "private_network", ip: "10.11.12.100"
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "1024"
  end

  config.vm.provision "shell", privileged: false, inline: <<-SHELL
    wget -q -O - https://jenkins-ci.org/debian/jenkins-ci.org.key | sudo apt-key add -
    sudo sh -c 'echo deb http://pkg.jenkins-ci.org/debian binary/ > /etc/apt/sources.list.d/jenkins.list'
    sudo apt-get update
    sudo apt-get install openjdk-7-jdk jenkins maven git mercurial subversion -y

    # Run Jenkins as the vagrant user
    sudo sed -i 's/^JENKINS_USER.*/JENKINS_USER=vagrant/;s/^JENKINS_GROUP.*/JENKINS_GROUP=vagrant/' /etc/default/jenkins
    sudo chown -R vagrant.vagrant /var/cache/jenkins /var/lib/jenkins /var/log/jenkins

    # configure settings.xml as mentioned in the prerequisites
    mkdir ~/.m2 > /dev/null 2>&1
    cat <<EOF > ~/.m2/settings.xml
<settings>
  <profiles>
    <profile>
      <id>compiler</id>
        <properties>
          <JAVA_1_7_HOME>/usr/lib/jvm/java-7-openjdk-amd64</JAVA_1_7_HOME>
        </properties>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>compiler</activeProfile>
  </activeProfiles>
</settings>
EOF

    # Set HUDSON_HOME for maven builds
    echo 'export HUDSON_HOME=/var/lib/jenkins' > ~/.profile

    # Stop the jenkins service so that hpi:run doesn't complain about 8080 port
    sudo service jenkins stop
    sudo update-rc.d jenkins disable

    # Configuring dev.deveo.com hostname
    if ! grep "^10.11.12.13 dev.deveo.com$" /etc/hosts &> /dev/null ; then
        echo '10.11.12.13 dev.deveo.com' | sudo tee -a /etc/hosts > /dev/null
    fi
  SHELL
end

