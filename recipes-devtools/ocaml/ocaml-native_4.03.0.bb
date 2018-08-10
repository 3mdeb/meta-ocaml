DESCRIPTION = "Objective Caml Compiler"
SECTION = "devel"
LICENSE = "QPL"

SRC_URI = " \
    https://github.com/ocaml/ocaml/archive/${PV}.tar.gz \
    file://ocaml-pathfixer \
    "

LIC_FILES_CHKSUM = "file://LICENSE;md5=1d53f1a1639ae7a362cf05c3a6c466c2"

SRC_URI[md5sum] = "4ddf4977de7708f11adad692c63e87ec"
SRC_URI[sha256sum] = "fef80a338099bffc56e4d1ef35146336195a4a9e2086e8cd186c402805503a0a"


inherit native

do_configure () {
    cd ${S}
    ./configure -prefix ${D} \
                -bindir ${D}${bindir} \
                -libdir ${D}${libdir} \
                -mandir ${D}${mandir} \
                -host ${BUILD_SYS} \
                -verbose \
                -no-graph 
}

do_compile() {
	oe_runmake world
    oe_runmake bootstrap
    oe_runmake opt
    oe_runmake opt.opt
}

OCAML_BYTECODE_APPS = " \
    ocaml \
    ocamlc \ 
    ocamlcp \ 
    ocamldebug \
    ocamldep \ 
    ocamldoc \ 
    ocamllex \ 
    ocamlmklib \ 
    ocamlmktop \ 
    ocamlobjinfo \  
    ocamlopt \ 
    ocamloptp \ 
    ocamlprof \
    "

do_install() {
    oe_runmake install

    # OCaml bytecode applications contain a hard-coded 'ocamlrun' path which does
    # not work well with OE, where they will be copied to individual directories
    # for each OCaml application. The 'pathfixer' script will take care of this
    install -m 0755 ${WORKDIR}/ocaml-pathfixer ${D}${bindir}
    cd ${D}${bindir}
    for i in ${OCAML_BYTECODE_APPS}; do
        mv ${i} _${i}
        ln -s ocaml-pathfixer ${i}
    done

}

#                -sysroot ${STAGING_DIR_NATIVE} 
