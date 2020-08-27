FROM stilliard/pure-ftpd

RUN mkdir -p /home/username
COPY ./data /home/username

# startup
CMD /run.sh -l puredb:/etc/pure-ftpd/pureftpd.pdb -E -j -R -P $PUBLICHOST
