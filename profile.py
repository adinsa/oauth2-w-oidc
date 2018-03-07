"""A git repository-based CloudLab profile containing two nodes running Ubuntu.""" 

# Import the Portal object.
import geni.portal as portal
# Import the ProtoGENI library.
import geni.rspec.pg as pg
# Import the Emulab specific extensions.
import geni.rspec.emulab as emulab

# Create a portal object,
pc = portal.Context()

# Create a Request object to start building the RSpec.
request = pc.makeRequestRSpec()

# Setup 'oidc-server' node
server_node = request.XenVM('oidc-server')
server_node.routable_control_ip = True
server_node.disk_image = 'urn:publicid:IDN+apt.emulab.net+image+emulab-ops//UBUNTU14-10-64-STD'
server_node.Site('Site 1')
server_node.addService(pg.Execute(shell="sh", command="/local/repository/setup.sh oidc-server"))

# Setup 'simple-web-app' node
simple_web_app_node = request.XenVM('simple-web-app')
simple_web_app_node.routable_control_ip = True
simple_web_app_node.disk_image = 'urn:publicid:IDN+apt.emulab.net+image+emulab-ops//UBUNTU14-10-64-STD'
simple_web_app_node.Site('Site 1')
simple_web_app_node.addService(pg.Execute(shell="sh", command="/local/repository/setup.sh simple-web-app"))

# Print the generated rspec
pc.printRequestRSpec(request)

