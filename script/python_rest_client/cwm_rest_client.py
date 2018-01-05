import requests

HEADERS = {u'Content-Type':u'application/json'}

class CwmRestClient:

  def __init__(self, host, port):
    self.host = host
    self.port = port

  def __url(self, action, host = "", port = -1):
    query = ""
    if host != "" and port != -1:
      query = '?host={0}&port={1}'.format(host, port)
    return u'http://{0}:{1}/api/{2}{3}'.format(self.host, self.port, action, query)

  def __get(self, action, host = "", port = -1):
    return requests.request(method=u'GET', url=self.__url(action, host, port), headers=HEADERS).json()

  def __post(self, action, json):
    requests.request(method=u'POST', headers=HEADERS, url=self.__url(action), json=json)

  def servers(self):
    return self.__get('servers')

  def workers(self, host = "", port = -1):
    return self.__get('workers', host, port)
  
  def jobs(self, host = "", port = -1):
    return self.__get('jobs', host, port)

  def run(self, job, hostPorts = []):
    self.__post('run', json={
      u'job': job, 
      u'hostPorts': hostPorts})

  def shutdown(self, hostPorts = []):
    self.__post('shutdown', json=hostPorts)

def main():
  client = CwmRestClient("fnode404", 8080)
  print "servers: ", client.servers()

  # client.run(job = [u'python', u'/gpfs/users/weliu/tools/longtime.py'],
  #           hostPorts = [u'fnode408:2555'])
  # client.run([u'python', u'/gpfs/users/weliu/tools/longtime.py'])

  # print "workers: ", client.workers()
  # print "fnode408 workers: ", client.workers("fnode408", 2555)
  # print "jobs: ", client.jobs()
  # print "fnode408 jobs: ", client.jobs("fnode408", 2555)

  # client.shutdown([u'fnode408:2555'])
  client.shutdown()


if __name__ == "__main__":
  main()
