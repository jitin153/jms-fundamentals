1. Navigate to "\apache_artemis_folder\bin\mybroker\etc\"
2. Open artemis-users.properties file
3. Add desired roles below the admin user(already exisiting) & save the file.
	Example:
	clinicaluser=clinicaluserpassword
	eligibilityuser=eligibilityuserpassword
4. Now open artemis-roles.properties file
5. Add desired roles below the amq role(already exisiting) & save the file.
	Example:
	clinicalrole=clinicaluser
	eligibilityrole=eligibilityuser
6. Now open broker.xml
7. Goto <security-settings> tag & copy complete existing <security-setting>...</security-setting> tag and paste above to the existing <security-setting> tag. The existing security setting is very much generic & it applies to all the topics & queues.
8. Replace # symbol in match attribute of <security-setting> tag with 'jitin.queues.request.#' (Refer jndi.propertis file for this queue name pattern).
	Example:
	<security-setting match="jitin.queues.request.#">
            <permission type="createNonDurableQueue" roles="clinicalrole,eligibilityrole"/>
            <permission type="deleteNonDurableQueue" roles="clinicalrole,eligibilityrole"/>
            <permission type="createDurableQueue" roles="clinicalrole,eligibilityrole"/>
            <permission type="deleteDurableQueue" roles="clinicalrole,eligibilityrole"/>
            <permission type="createAddress" roles="clinicalrole,eligibilityrole"/>
            <permission type="deleteAddress" roles="clinicalrole,eligibilityrole"/>
            <permission type="consume" roles="eligibilityrole"/>
            <permission type="browse" roles="clinicalrole"/>
            <permission type="send" roles="clinicalrole"/>
            <!-- we need this otherwise ./artemis data imp wouldn't work -->
            <permission type="manage" roles="amq"/>
    </security-setting>
9. Copy the newly created <security-setting> tag & paste below newly created security setting.
10. Replace # symbol in match attribute of this just pasted <security-setting> tag with 'jitin.queues.reply.#' (Refer jndi.propertis file for this queue name pattern).
	Example:
	<security-setting match="jitin.queues.reply.#">
            <permission type="createNonDurableQueue" roles="eligibilityrole"/>
            <permission type="deleteNonDurableQueue" roles="eligibilityrole"/>
            <permission type="createDurableQueue" roles="eligibilityrole"/>
            <permission type="deleteDurableQueue" roles="eligibilityrole"/>
            <permission type="createAddress" roles="eligibilityrole"/>
            <permission type="deleteAddress" roles="eligibilityrole"/>
            <permission type="consume" roles="clinicalrole"/>
            <permission type="browse" roles="eligibilityrole"/>
            <permission type="send" roles="eligibilityrole"/>
            <!-- we need this otherwise ./artemis data imp wouldn't work -->
            <permission type="manage" roles="amq"/>
    </security-setting>

NOTE: Kindly refer the 'sample-broker-config-files' folder for the broker specific configuration files.