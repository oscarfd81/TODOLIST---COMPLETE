package com.oscar.todo_rest.dto;

public class DashboardStats {
    private long total;
    private long pendiente;
    private long enProceso;
    private long hecho;
    private long noHecho;
    private long importantes;

    public DashboardStats() {}

    public DashboardStats(long total, long pendiente, long enProceso,
                          long hecho, long noHecho, long importantes) {
        this.total = total; this.pendiente = pendiente;
        this.enProceso = enProceso; this.hecho = hecho;
        this.noHecho = noHecho; this.importantes = importantes;
    }

    public long getTotal()       { return total; }
    public long getPendiente()   { return pendiente; }
    public long getEnProceso()   { return enProceso; }
    public long getHecho()       { return hecho; }
    public long getNoHecho()     { return noHecho; }
    public long getImportantes() { return importantes; }

    public void setTotal(long v)       { this.total = v; }
    public void setPendiente(long v)   { this.pendiente = v; }
    public void setEnProceso(long v)   { this.enProceso = v; }
    public void setHecho(long v)       { this.hecho = v; }
    public void setNoHecho(long v)     { this.noHecho = v; }
    public void setImportantes(long v) { this.importantes = v; }
}
