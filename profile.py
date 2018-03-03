"""A git repository-based Cloudlab profile."""

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

# Node node1
node1 = request.XenVM('node1')
node1.routable_control_ip = True
node1.disk_image = 'urn:publicid:IDN+apt.emulab.net+image+emulab-ops//UBUNTU14-10-64-STD'
node1.Site('28')

# Install and execute setup script that is contained in the repository.
node.addService(pg.Execute(shell="sh", command="/local/repository/setup.sh"))

# Print the generated rspec
pc.printRequestRSpec(request)

