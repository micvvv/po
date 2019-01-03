FROM airhacks/glassfish
MAINTAINER Mikhail Vasilko <micvvv@gmail.com>

COPY target/po.war ${DEPLOYMENT_DIR}

# Add healthcheck
HEALTHCHECK --interval=5m --timeout=3s CMD curl -f http://localhost:8080/po/api/order || exit 1

