Deploy pe WildFly:

$JBOSS_HOME\bin\standalone.{sh|bat} & 
[ -b 0.0.0.0 spre a putea fi accesat de la orice masina ]

copy .../1ejbSession.jar in $JBOSS_HOME/standalone/deployments/
In directorul deployments apare un fisier care indica deploy reusit sau nu.

Din browser http://localhost:9990/console se are acces la consola de administrare WildFly. 

Deploy pe Glassfish:
$GF_HOME\glassfish\bin\asadmin &
asadmin>start-domain

asadmin> deploy .../1ejbSession.jar 
sau
copy .../1ejbSession.jar in $GF_HOME/glassfish/domains/domain1/autodeploy
In directorul autodeploy apare un fisier care indica deploy-ul reusit sau nu.

Din browser http://localhost:4848 se are acces la consola de administrare GlassFish. 
